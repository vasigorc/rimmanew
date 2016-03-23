/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.ejbs.OutsideContainerJpaTests;
import com.vgorcinschi.rimmanew.rest.services.AppointmentResourceService;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class GetAppsExperimentalIntegrationTests {

    private final AppointmentRepository repository;
    private final AppointmentResourceService service;

    public GetAppsExperimentalIntegrationTests() {
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
    public void onlyTimeParamRequestTest() {
        Response response = service.getAppointments("", "11:00:00", "", "", 2, 2);
        assertTrue(response.hasEntity());
        System.out.println("\nonlyTimeParamRequestTest JSON: " + response.getEntity().toString());
    }

    @Test
    public void emptyRequestTest() {
        Response response = service.getAppointments("", "", "", "", 0, 10);
        assertTrue(response.hasEntity());
        System.out.println("\nemptyRequestTest JSON: " + response.getEntity().toString());
    }

    @Test
    public void onlyDateParamRequestTest() {
        Response response = service.getAppointments("2016-02-17", "", "", "", 0, 10);
        assertTrue(response.hasEntity());
        System.out.println("\nonlyDateParamRequestTest JSON: " + response.getEntity().toString());
    }

    @Test
    public void onlyNameParameRequestTest() {
        Response response = service.getAppointments("", "", "", "Varvara", 0, 10);
        assertTrue(response.hasEntity());
        System.out.println("\nonlyNameParameRequestTest JSON: " + response.getEntity().toString());
    }

    @Test
    public void typeAndOffsetParamsRequestTest() {
        Response response = service.getAppointments("", "", "massage", "", 5, 3);
        assertTrue(response.hasEntity());
        System.out.println("\ntypeAndOffsetParamsRequestTest JSON: " + response.getEntity().toString());
    }

    @Test
    public void dateAndTimeParamsRequestTest() {
        Response response = service.getAppointments("2016-02-03", "11:00", "", "", 0, 10);
        assertTrue("We are "
                + " testing whether the URI contains the"
                + " date parameter", response.getEntity().toString().contains("date"));
        System.out.println("\ndateAndTimeParamsRequestTest JSON: " + response.getEntity().toString());
    }

    @Test
    public void dateAndTypeSizeRequestTest() {
        Response response = service.getAppointments("2016-01-19", "", "massage", "", 0, 2);
        assertTrue("We are "
                + " testing whether the URI contains the"
                + " date parameter", response.getEntity().toString().contains("start"));
        System.out.println("\ndateAndTypeSizeRequestTest JSON: " + response.getEntity().toString());
    }
}
