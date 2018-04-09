/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.InMemoryRepository;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author vgorcinschi
 */
@RunWith(Arquillian.class)
public class ArquillianInjectionTesting {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "injectionTest.war")
                .addClasses(AppointmentService.class,
                        DefaultAppointmentService.class,
                        AppointmentRepository.class,
                        InMemoryAppointmentRepository.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    @Default
    private AppointmentService service;

    @Inject
    @InMemoryRepository
    private AppointmentRepository repository;

    @Test
    public void serviceIsNotNullTest() {
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
        assertEquals(service.findByType("massage").size(),3);
    }
    
    @Test
    public void testRetrieveByDateAndTime(){
        assertEquals(service.findByDateAndTime(localToSqlDate(LocalDate.now()
                .plusDays(5)), localToSqlTime(LocalTime.of(10, 00)))
                .getClientMessage(), "Telephonez moi SVP");
    }
    
    @Test
    public void testRetrieveByDateAndType(){
        assertEquals(service.findByDateAndType(valueOf(LocalDate.of(2015, 12, 14)),
                "manicure").size(), 1);
    }
    
    @Test
    public void testGetAll(){
        assertEquals(repository.getAll().size(), 6);
    }
}