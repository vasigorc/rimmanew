/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.ejbs.OutsideContainerJpaTests;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.rest.services.AppointmentResourceService;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import static java.time.LocalDate.of;
import java.time.LocalTime;
import javax.ws.rs.BadRequestException;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class DeleteAppointmentsRestTests {

    private final AppointmentRepository repository;
    private final AppointmentResourceService service;

    public DeleteAppointmentsRestTests() {
        OutsideContainerJpaTests tests = new OutsideContainerJpaTests();
        this.repository = tests.getRepository();
        this.service = new AppointmentResourceService();
        this.service.setRepository(repository);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void inexistingAppointmentRequest(){
        try {
            service.deleteAppointment(2);
        } catch (Exception e) {
            assertThat(e, instanceOf(BadRequestException.class));
            System.out.println("\n"+e.getMessage());
        }
    }
    
    @Test
    public void succesfullyDeletedAppointment(){
        Appointment appointment = new Appointment();
        appointment.setClientName("Uniquename");
        appointment.setDate(Java8Toolkit.localToSqlDate(of(2016, 04, 29)));
        appointment.setTime(Java8Toolkit.localToSqlTime(LocalTime.of(15,0)));
        appointment.setEmail("wber@jsdf.fd");
        appointment.setMessage("waxing");
        repository.add(appointment);
        Appointment retrieved = repository.getByName("Uniquename").get(0);
        System.out.println(service.deleteAppointment(retrieved.getId()).getEntity().toString());
        assertTrue(repository.getByName("Uniquename").isEmpty());
    }
}
