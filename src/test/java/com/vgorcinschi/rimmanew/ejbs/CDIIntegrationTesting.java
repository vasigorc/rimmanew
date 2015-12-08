/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.InMemoryRepository;
import com.vgorcinschi.rimmanew.model.AppointmentFormBean;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.jboss.weld.context.bound.BoundSessionContext;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ejb.SupportEjb;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
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

    @Inject
    private BoundSessionContext sessionContext;

    public CDIIntegrationTesting() {
    }

    @Before
    public void setUp() {
        Map<String, Object> myMap = new HashMap<>();
        sessionContext.associate(myMap);
        sessionContext.activate();
    }

    @After
    public void tearDown() {
        sessionContext.invalidate();
        sessionContext.deactivate();
    }

    @Test
    public void managedBeanPresentTest() {
        assertNotNull(form);
    }
    /**
     *
     */
    @Test
    @Ignore
    public void ejbCalledFromMBTest() {
        form.setSelectedDate(localToSqlDate(LocalDate.now().plusDays(10)));
        System.out.println(service.findByDate(localToSqlDate(LocalDate.now().plusDays(10))).get(0).getClientName());
        assertTrue(form.getBookedAlready().size() > 0);
    }
    
    @Test
    public void testSessionContext(){
        form.setDatePickerActivated(true);
        assertTrue(form.isDatePickerActivated());
    }
}