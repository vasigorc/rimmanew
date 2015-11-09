/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class ServiceMethodsTests {
    private InMemoryAppointmentRepository repository;
    private DefaultAppointmentService service;
    
    public ServiceMethodsTests() {
        repository = new InMemoryAppointmentRepository();
        repository.add(new Appointment(1, valueOf(LocalDate.of(2015, 11, 12)),localToSqlTime(LocalTime.of(17, 20)),
        "massage","Anna Filipovna", "","Will be there"));
        service = new DefaultAppointmentService(repository);
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void entryCanBeRetrievedViaService(){
        assertEquals(service.findById(1).getClientName(), "Anna Filipovna");
    }
    
    @Test
    public void entryCanBeDeleted(){
        Appointment dummy = new Appointment(7, valueOf(LocalDate.of(2015, 11, 18)),localToSqlTime(LocalTime.of(11, 10)),
        "manicure","Mme Lefebvre", "","J'arrive");
        repository.add(dummy);
        service.deleteOne(dummy);
        assertNull(service.findById(7));
    }
}
