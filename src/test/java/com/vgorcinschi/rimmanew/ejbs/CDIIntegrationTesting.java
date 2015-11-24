/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.InMemoryRepository;
import com.vgorcinschi.rimmanew.model.AppointmentFormBean;
import javax.ejb.EJB;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ejb.SupportEjb;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author vgorcinschi
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({DefaultAppointmentService.class,
    InMemoryAppointmentRepository.class, AppointmentFormBean.class})
@SupportEjb
public class CDIIntegrationTesting {

    @EJB
    private AppointmentService service;

    @EJB
    @InMemoryRepository
    private AppointmentRepository repository;

    @Inject
    AppointmentFormBean form;

    public CDIIntegrationTesting() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void managedBeanPresentTest(){
        assertNotNull(form);
    }
}
