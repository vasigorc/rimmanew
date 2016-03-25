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
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import static org.hamcrest.Matchers.instanceOf;
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
    private String storedDate;

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
        storedDate = "2016-06-18";
    }

    @After
    public void tearDown() {
    }

    @Test
    public void successfullyUpdatedDay() {
        Response response = service.updateSpecialDay(storedDate, "9:00", "15:00",
                "12:00", "13:00", "30", "false", "Short day", "true");
        System.out.println("\nsuccessfullyUpdatedDay: " + response.getEntity().toString());
        assertTrue("We are "
                + " testing whether the URI contains the"
                + " specialdays path", response.getEntity().toString().contains("specialdays"));
    }

    @Test
    public void aMandatoryFieldSkippedInRequestTest() {
        try {
            Response response = service.updateSpecialDay(storedDate, "9:00", "15:00",
                    "12:00", "13:00", "30", "", "Short day", "true");
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\naMandatoryFieldSkippedInRequestTest: " + e.getMessage());
        }
    }

    @Test
    public void invalidDayFormatRequestTest() {
        try {
            Response response = service.updateSpecialDay("11th of January", "9:00", "15:00",
                    "12:00", "13:00", "30", "false", "Short day", "true");
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\ninvalidDayFormatRequestTest: " + e.getMessage());
        }
    }

    @Test
    public void rethrownExceptionFromcheckAndBuildTest() {
        /*
         It should be sufficient to test that at least one exception 
         is re-thrown. The rest of them function in the similar manner.
         In this case are testing the scenario when despite the requested
         Special Day not being 'blocked' no start and end hours are
         provided
         */
        try {
            Response response = service.updateSpecialDay(storedDate, null, null,
                    "12:00", "13:00", "30", "false", "Short day", "true");
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\nrethrownExceptionFromcheckAndBuildTest: " + e.getMessage());
        }
    }

    @Test
    public void requestedForUpdateDateDoesnExistTest() {
        //2015-12-20 isn't stored in the DB
        try {
            Response response = service.updateSpecialDay("2015-12-20", "9:00", "15:00",
                    "12:00", "13:00", "30", "false", "Short day", "true");
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\nrequestedForUpdateDateDoesnExistTest: " + e.getMessage());
        }
    }

    @Test
    public void requestWithAllowConflictsNotAllowedTest() {
        //we know that there are appointments on 2016-01-19
        try {
            Response response = service.updateSpecialDay("2016-01-19", "9:00", "15:00",
                    "12:00", "13:00", "30", "false", "Short day", "false");
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\nrequestWithAllowConflictsNotAllowedTest: " + e.getMessage());
        }
    }
}
