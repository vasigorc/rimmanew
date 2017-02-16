/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.ejbs.OCFutureAppointmentsRepository;
import com.vgorcinschi.rimmanew.ejbs.OutsideContainerJpaTests;
import com.vgorcinschi.rimmanew.rest.services.AppointmentResourceService;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeConverter;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsString;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class AppointmentResourceServiceTests {

    private final AppointmentResourceService service;
    private final AppointmentRepository repository;
    private final SqlDateConverter dateConverter = new SqlDateConverter();
    private final SqlTimeConverter timeConverter = new SqlTimeConverter();
    private final org.apache.logging.log4j.Logger log = LogManager.getLogger();
    private final Map<String, Object> configs = new HashMap<>(1);
    private final JsonBuilderFactory factory;

    public AppointmentResourceServiceTests() {
        OutsideContainerJpaTests tests = new OutsideContainerJpaTests();
        this.repository = tests.getRepository();
        this.service = new AppointmentResourceService();
        this.service.setRepository(repository);
        this.service.setFutureRepository(new OCFutureAppointmentsRepository());
        this.configs.put(JsonGenerator.PRETTY_PRINTING, true);
        this.factory = Json.createBuilderFactory(configs);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = BadRequestException.class)
    public void testAPastDateAttempt() {
        JsonObject value = factory.createObjectBuilder()
                .add("id", "")
                .add("date",
                        Java8Toolkit.localToSqlDate(LocalDate.of(2014, 01, 12)).toString())
                .add("time", "15:00")
                .add("type", "massage")
                .add("clientName", "Rimma")
                .add("email", "valid@email.ca")
                .add("message", "any").build();
        log.info("Running the test testAPastDateAttempt");
        InputStream is = new ByteArrayInputStream(value.toString().getBytes());
        service.bookAppointment(is);
    }

    @Test
    public void testADateMoreThen3MonthsInTheFuture() {
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("id", "")
                    .add("date",
                            Java8Toolkit.localToSqlDate(LocalDate.of(2019, 01, 12)).toString())
                    .add("time", "15:00")
                    .add("type", "massage")
                    .add("clientName", "Rimma")
                    .add("email", "valid@email.ca")
                    .add("message", "any").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.bookAppointment(is);
        } catch (BadRequestException e) {
            assertEquals(e.getMessage(), "You cannot take appointment on a "
                    + "past date or on a day which is 3+ months in the future.");
        }
    }

    @Test
    public void testAnEmptyClientName() {
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("id", "")
                    .add("date",
                            Java8Toolkit.localToSqlDate(LocalDate.now().plusMonths(2)).toString())
                    .add("time", "15:00")
                    .add("type", "massage")
                    .add("clientName", "Rimma")
                    .add("email", "valid@email.ca")
                    .add("message", "any").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.bookAppointment(is);
        } catch (BadRequestException e) {
            assertEquals(e.getMessage(), "The request has been rejected because "
                    + "you have not provided either type of appointment, client "
                    + "name or client email");
        }
    }

    @Test//(expected = InternalServerErrorException.class)
    public void integrationTestException() {
        JsonObject value = factory.createObjectBuilder()
                .add("id", "")
                .add("date",
                        Java8Toolkit.localToSqlDate(LocalDate.now()
                                .plusDays((long) new Random().nextInt(90))).toString())
                .add("time", "15:00")
                .add("type", "massage")
                .add("clientName", "Rimma")
                .add("email", "valid@email.ca")
                .add("message", "any").build();
        InputStream is = new ByteArrayInputStream(value.toString().getBytes());
        System.out.println(service.bookAppointment(is).getEntity().toString());
    }

    @Test
    public void dateConverterProviderTest() {
        assertNotNull(dateConverter.fromString("2016-02-03"));
        Assert.assertThat(dateConverter.fromString("2016-02-03"), Matchers.instanceOf(java.sql.Date.class));
    }

    @Test
    public void timeConverterProviderTest() {
        assertNotNull(timeConverter.fromString("15:00"));
        Assert.assertThat(timeConverter.fromString("15:00"), Matchers.instanceOf(java.sql.Time.class));
    }

    @Test
    public void timeConverterProviderNonASCIIsafe() {
        assertNotNull(timeConverter.fromString("15%3A00"));
        Assert.assertThat(timeConverter.fromString("15%3A00"), Matchers.instanceOf(java.sql.Time.class));
    }

    @Test(expected = BadRequestException.class)
    public void zeroSizeAppointmentsRequestTest() {
        service.getAppointments("", "", "", "", 5, 0,
                "true", "false");
    }

    @Test
    public void offsetTooBigRequestTest() {
        try {
            service.getAppointments("", "", "", "", 50, 5, "true", "false");
        } catch (BadRequestException e) {
            assertEquals(e.getMessage(), "There are less appointments in the "
                    + "system than you have requested");
        }
    }

    @Test
    public void jsonAllAppointmentsRootNameTest() throws JsonProcessingException {
        Response response = service.getAppointments("", "", "", "", 10, 5,
                "true", "false");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String output = mapper.writeValueAsString(response.getEntity());

        MatcherAssert.assertThat(output, containsString("appointments"));
        System.out.println(output);
    }

    @Test
    public void updateAppointmentTest() {
        JsonObject value = factory.createObjectBuilder()
                .add("id", "35")
                .add("date",
                        Java8Toolkit.localToSqlDate(LocalDate.now()
                                .plusDays((long) new Random().nextInt(90))).toString())
                .add("time", "15:00")
                .add("type", "massage")
                .add("clientName", "Rimma")
                .add("email", "valid@email.ca")
                .add("message", "any")
                .add("past", "false")
                .add("noShow", "false").build();
        InputStream is = new ByteArrayInputStream(value.toString().getBytes());
        java.sql.Date date = localToSqlDate(of(2016, 9, 19));
        Response response = service.updateAppointment(35, is);
        assertTrue(response.getEntity().toString().contains("link"));
        System.out.println("\nupdateAppointmentTest:\n" + response.getEntity().toString() + "\n");
    }

    @Test
    public void getOnlyFutureAppointmentsTest() throws JsonProcessingException {
        Response response = service.getAppointments("", "", "", "", 0, 5,
                "false", "false");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String output = mapper.writeValueAsString(response.getEntity());

        MatcherAssert.assertThat(output, containsString("appointments"));
        System.out.println("\ngetOnlyFutureAppointmentsTest\n");
        System.out.println(output);
    }
}
