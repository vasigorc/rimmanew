/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.ejbs.OutsideContainerJpaTests;
import com.vgorcinschi.rimmanew.ejbs.SpecialDayRepository;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.rest.services.SpecialDayResourceService;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import static java.time.LocalDate.of;
import java.time.LocalTime;
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
public class SpecialDayDeleteTests {

    private final SpecialDayResourceService service;
    private final SpecialDayRepository repository;
    private final AppointmentRepository appointmentsRepository;

    public SpecialDayDeleteTests() {
        this.service = new SpecialDayResourceService();
        OutsideContainerJpaTests tests = new OutsideContainerJpaTests();
        this.appointmentsRepository = tests.getRepository();
        this.repository = new OutsideContainerSpecialDayRepository();
        service.setAppointmentsRepository(appointmentsRepository);
        service.setRepository(repository);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void successfullyDeletedSpecialDay() {
        /*
         We need to save a date that is not in the DB
         */
        SpecialDay specialDay = new SpecialDay(localToSqlDate(of(2016, 03, 01)),
                localToSqlTime(LocalTime.of(12, 0)), localToSqlTime(LocalTime.of(18, 0)), null, null,
                0, false, null);
        //save the special day first
        repository.setSpecialDay(specialDay);
        //confirm that it is there
        assertNotNull(repository.getSpecialDay(of(2016, 03, 01)));
        //delete it using the rest service simulation
        service.deleteSpecialDay("2016-03-01", "true");
        //confirm that the special day is no longer there
        assertNull(repository.getSpecialDay(of(2016, 03, 01)));
    }

    @Test
    public void aMandatoryFieldSkippedInRequestTest() {
        try {
            Response response = service.deleteSpecialDay("2016-05-15", null);
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\naMandatoryFieldSkippedInRequestTest: " + e.getMessage());
        }
    }

    @Test
    public void invalidDayFormatRequestTest() {
        try {
            Response response = service.deleteSpecialDay("11th of January", "true");
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\ninvalidDayFormatRequestTest: " + e.getMessage());
        }
    }

    @Test
    public void requestedForUpdateDateDoesnExistTest() {
        //2015-12-20 isn't stored in the DB
        try {
            Response response = service.deleteSpecialDay("2015-12-20", "true");
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
            Response response = service.deleteSpecialDay("2016-01-19","false");
        } catch (Exception e) {
            assertThat("The caught exception must be of type BadRequestException",
                    e, instanceOf(BadRequestException.class));
            System.out.println("\nrequestWithAllowConflictsNotAllowedTest: " + e.getMessage());
        }
    }
}
