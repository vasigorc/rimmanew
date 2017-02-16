/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.InMemoryRepository;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import java.time.LocalDate;
import javax.ejb.EJB;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ejb.SupportEjb;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author vgorcinschi
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({DefaultAppointmentService.class,
    InMemoryAppointmentRepository.class})
@SupportEjb
public class EJBDeleteTest {
    
    @EJB
    private AppointmentService service;

    @EJB
    @InMemoryRepository
    private AppointmentRepository repository;
    
    public EJBDeleteTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

   @Test
    public void testDeleteBeforeDate(){
        service.deleteAllBefore(localToSqlDate(LocalDate.now()
                .plusDays(7)));
        assertEquals(service.findByDate(localToSqlDate(LocalDate.now()
                .plusDays(5))).size(), 0);
    }
}
