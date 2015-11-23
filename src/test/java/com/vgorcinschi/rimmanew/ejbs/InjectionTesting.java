/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.InMemoryRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.ejb.EJB;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ejb.SupportEjb;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import org.junit.runner.RunWith;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.*;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author vgorcinschi
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({DefaultAppointmentService.class,
    InMemoryAppointmentRepository.class})
@SupportEjb
public class InjectionTesting {

    @EJB
    private AppointmentService service;

    @EJB
    @InMemoryRepository
    private AppointmentRepository repository;

    public InjectionTesting() {
    }

    @Before
    public void setUp() {
        service.save(new Appointment(2, valueOf(LocalDate.of(2015, 11, 14)), localToSqlTime(LocalTime.of(11, 30)),
                "manicure", "Aglaia Ivanovna", "cratita@mail.md", "Vin, vin"));
        service.save(new Appointment(3, localToSqlDate(LocalDate.now()
                .plusDays(10)), localToSqlTime(LocalTime.of(11, 30)),
                "massage", "Tamara Fedorovna", "casserole@yahoo.qc", "J'y viendrais"));
        service.save(new Appointment(4, localToSqlDate(LocalDate.now()
                .plusDays(5)), localToSqlTime(LocalTime.of(11, 30)),
                "massage", "Isabelle Legrand", "", "Telephonez moi SVP"));
        service.save(new Appointment(1, localToSqlDate(LocalDate.now()
                .plusDays(5)), localToSqlTime(LocalTime.of(11, 00)),
                "waxing", "Danielle Labrave", "ahdjdsa@hakdfs.ds", "A tantot"));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void ServiceNotNullTest() {
        assertNotNull(service);
    }

    @Test
    public void RepositoryNotNull() {
        assertNotNull(repository);
    }

    @Test
    public void testRetrieveAppointmentByDate() {
        assertEquals(service.findByDate(localToSqlDate(LocalDate.now()
                .plusDays(10))).size(), 1);
        assertEquals(service.findByDate(localToSqlDate(LocalDate.now()
                .plusDays(10))).get(0).getClientName(), "Tamara Fedorovna");
    }
    
    @Test
    public void testRetrieveByType(){
        assertEquals(service.findByType("massage").size(),2);
    }
    
    @Test
    public void testRetrieveByDateAndTime(){
        assertEquals(service.findByDateAndTime(localToSqlDate(LocalDate.now()
                .plusDays(5)), localToSqlTime(LocalTime.of(11, 00)))
                .getClientMessage(), "A tantot");
    }
    
    @Test
    public void testRetrieveByDateAndType(){
        assertEquals(service.findByDateAndType(valueOf(LocalDate.of(2015, 11, 14)),
                "manicure").size(), 1);
    }
    
    @Test
    public void testDeleteBeforeDate(){
        service.deleteAllBefore(localToSqlDate(LocalDate.now()
                .plusDays(7)));
        assertEquals(service.findByDate(localToSqlDate(LocalDate.now()
                .plusDays(5))).size(), 0);
    }
    
    @Test
    public void testGetAll(){
        assertEquals(service.findAll().size(),4);
    }
}
