/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

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
    private AppointmentRepository repository;
    
    public InjectionTesting() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
   
     @Test
     public void ServiceNotNullTest() {
         assertNotNull(service);
     }    
}
