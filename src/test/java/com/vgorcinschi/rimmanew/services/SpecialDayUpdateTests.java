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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author vgorcinschi
 */
public class SpecialDayUpdateTests {

    private final SpecialDayResourceService service;
    private final SpecialDayRepository repository;
    private final AppointmentRepository appointmentsRepository;
    private String storedDate;
    private final Map<String, Object> configs = new HashMap<>(1);
    private final JsonBuilderFactory factory;

    public SpecialDayUpdateTests() {
        this.service = new SpecialDayResourceService();
        OutsideContainerJpaTests tests = new OutsideContainerJpaTests();
        this.appointmentsRepository = tests.getRepository();
        this.repository = new OutsideContainerSpecialDayRepository();
        service.setAppointmentsRepository(appointmentsRepository);
        service.setRepository(repository);
        this.configs.put(JsonGenerator.PRETTY_PRINTING, true);
        this.factory = Json.createBuilderFactory(configs);
    }

    @Before
    public void setUp() {
        storedDate = "2016-09-02";
    }

    @After
    public void tearDown() {
    }

    @Test
    public void successfullyUpdatedDay() {
        JsonObject value = factory.createObjectBuilder()
                .add("date", storedDate)
                .add("startAt", "9:00")
                .add("endAt", "15:00")
                .add("breakStart", "12:00")
                .add("breakEnd", "12:30")
                .add("duration", "30")
                .add("blocked", "false")
                .add("message", "Short day")
                .add("allowConflicts", "true").build();
        InputStream is = new ByteArrayInputStream(value.toString().getBytes());
        Response response = service.updateSpecialDay(storedDate, is);
        System.out.println("\nsuccessfullyUpdatedDay: " + response.getEntity().toString());
        assertTrue("We are "
                + " testing whether the URI contains the"
                + " specialdays path", response.getEntity().toString().contains("specialdays"));
    }

    @Test
    public void aMandatoryFieldSkippedInRequestTest() {
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("date", storedDate)
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "13:00")
                    .add("duration", "30")
                    .add("blocked", "")
                    .add("message", "Short day")
                    .add("allowConflicts", "true").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.updateSpecialDay(storedDate, is);
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\naMandatoryFieldSkippedInRequestTest: " + e.getMessage());
        }
    }

    @Test
    public void invalidDayFormatRequestTest() {
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("date", "11th of January")
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "13:00")
                    .add("duration", "30")
                    .add("blocked", "false")
                    .add("message", "Short day")
                    .add("allowConflicts", "true").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.updateSpecialDay(storedDate, is);
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
            JsonObject value = factory.createObjectBuilder()
                    .add("date", storedDate)
                    .add("startAt", "")
                    .add("endAt", "")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "13:00")
                    .add("duration", "30")
                    .add("blocked", "false")
                    .add("message", "Short day")
                    .add("allowConflicts", "true").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.updateSpecialDay(storedDate, is);
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
            JsonObject value = factory.createObjectBuilder()
                    .add("date", "2015-12-20")
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "13:00")
                    .add("duration", "30")
                    .add("blocked", "false")
                    .add("message", "Short day")
                    .add("allowConflicts", "true").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.updateSpecialDay(storedDate, is);
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
            JsonObject value = factory.createObjectBuilder()
                    .add("date", "2016-01-19")
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "13:00")
                    .add("duration", "30")
                    .add("blocked", "false")
                    .add("message", "Short day")
                    .add("allowConflicts", "false").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.updateSpecialDay(storedDate, is);
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\nrequestWithAllowConflictsNotAllowedTest: " + e.getMessage());
        }
    }
}
