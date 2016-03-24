/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.ejbs.OutsideContainerJpaTests;
import com.vgorcinschi.rimmanew.ejbs.SpecialDayRepository;
import com.vgorcinschi.rimmanew.rest.services.SpecialDayResourceService;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class SpecialDayUpdateTests {

    private final SpecialDayResourceService service;
    private final SpecialDayRepository repository;
    private final AppointmentRepository appointmentsRepository;

    public SpecialDayUpdateTests() {
        this.service = new SpecialDayResourceService();
        OutsideContainerJpaTests tests = new OutsideContainerJpaTests();
        this.appointmentsRepository = tests.getRepository();
        this.repository = new OutsideContainerSpecialDayRepository();
        service.setAppointmentsRepository(appointmentsRepository);
        service.setRepository(repository);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void successfullyUpdatedDay() {
        String storedDate = "2016-06-18";
        Response response = service.updateAppointment(12, storedDate, "9:00", "15:00",
                "12:00", "13:00", "30", "false", "Short day", "true");
        System.out.println("\nsuccessfullyUpdatedDay: " + response.getEntity().toString());
        assertTrue("We are "
                + " testing whether the URI contains the"
                + " specialdays path", response.getEntity().toString().contains("specialdays"));
    }
}
