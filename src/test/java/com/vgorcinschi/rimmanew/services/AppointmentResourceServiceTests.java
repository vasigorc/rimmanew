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
import com.vgorcinschi.rimmanew.ejbs.OutsideContainerJpaTests;
import com.vgorcinschi.rimmanew.rest.services.AppointmentResourceService;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeConverter;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import java.time.LocalDate;
import java.util.Random;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsString;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    public AppointmentResourceServiceTests() {
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

    @Test(expected = BadRequestException.class)
    public void testAPastDateAttempt() {
        service.bookAppointment(Java8Toolkit.localToSqlDate(LocalDate.of(2014, 01, 12)),
                "15:00", "massage", "Rimma",
                "valid@email.ca", "any");
    }

    @Test
    public void testADateMoreThen3MonthsInTheFuture() {
        try {
            service.bookAppointment(Java8Toolkit.localToSqlDate(LocalDate.of(2019, 01, 12)),
                    "15:00", "massage", "Rimma",
                    "valid@email.ca", "any");
        } catch (BadRequestException e) {
            assertEquals(e.getMessage(), "You cannot take appointment on a "
                    + "past date or on a day which is 3+ months in the future.");
        }
    }

    @Test
    public void testAnEmptyClientName() {
        try {
            service.bookAppointment(
                    Java8Toolkit.localToSqlDate(LocalDate.now().plusMonths(2)),
                    "15:00", "massage", "",
                    "valid@email.ca", "any");
        } catch (BadRequestException e) {
            assertEquals(e.getMessage(), "The request has been rejected because "
                    + "you have not provided either type of appointment, client "
                    + "name or client email");
        }
    }

    @Test//(expected = InternalServerErrorException.class)
    public void integrationTestException() {
        service.bookAppointment(
                Java8Toolkit.localToSqlDate(LocalDate.now().plusDays((long) new Random().nextInt(90))),
                "15:00", "massage", "Rimma",
                "valid%40email.ca", "any");
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

    @Test
    public void zeroSizeAppointmentsRequestTest() {
        try {
            service.getAppointments(5, 0);
        } catch (BadRequestException e) {
            assertEquals(e.getMessage(), "You haven't requested any appointments");
        }
    }
    
    @Test
    public void offsetTooBigRequestTest(){
        try {
            service.getAppointments(50, 5);
        } catch (BadRequestException e) {
            System.out.println(e.getMessage());
            assertEquals(e.getMessage(), "There are less appointments in the "
                    + "system than you have requested");            
        }
    }
    
    @Test
    public void jsonAllAppointmentsRootNameTest() throws JsonProcessingException{
        Response response = service.getAppointments(10, 5);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String output = mapper.writeValueAsString(response.getEntity());
        
        MatcherAssert.assertThat(output, containsString("appointments"));
        System.out.println(output);
    }
}
