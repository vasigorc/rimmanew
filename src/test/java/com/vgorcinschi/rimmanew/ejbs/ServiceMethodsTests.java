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
        service.save(new Appointment(2, valueOf(LocalDate.of(2015, 11, 14)),localToSqlTime(LocalTime.of(11, 30)),
        "manicure","Aglaia Ivanovna", "cratita@mail.md","Vin, vin"));
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void entryCanBeRetrievedViaService(){
        assertEquals(service.findById(1).getClientName(), "Danielle Labrave");
    }
    
    @Test
    public void entryCanBeDeleted(){        
        repository.update(new Appointment(7, valueOf(LocalDate.of(2015, 11, 18)),localToSqlTime(LocalTime.of(11, 10)),
        "manicure","Mme Lefebvre", "","J'arrive"));
        service.deleteOne(service.findById(7).getEntity());        
        assertTrue("The wrapper object must return true if it contains"
                + " no Appointment instance", service.findById(7).isEmpty());
    }
    
    @Test
    public void testSaveAppointmentWithExistingId(){
        service.save(new Appointment(3, valueOf(LocalDate.of(2015, 11, 11)),localToSqlTime(LocalTime.of(11, 00)),
        "massage","Nastasia Filipovna", "","Idu, idu"));
        assertEquals(service.findById(3).getClientName(), "Nastasia Filipovna");
    }
    
    @Test
    public void testFindByName(){
        assertEquals("Check the value of the 'saved' day of Month", 
                service.findByName("Aglaia Ivanovna").get(0).getDate().getDayOfMonth(), 
                14);
        System.out.println(service.findByName("Aglaia Ivanovna").get(0).getClientEmail());
    }
}
