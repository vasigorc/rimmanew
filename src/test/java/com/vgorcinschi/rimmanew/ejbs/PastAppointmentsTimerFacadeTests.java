/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgorcinschi.rimmanew.entities.Appointment;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author vgorcinschi
 */
public class PastAppointmentsTimerFacadeTests {

    private final PastAppointmentsTimerFacade facade;
    private final FutureAppointmentsRepository repository;
    private final CompanyProperties compProperties;

    public PastAppointmentsTimerFacadeTests() {
        this.facade = new PastAppointmentsTimerFacade();
        this.compProperties = new CompanyPropertiesImpl();
        this.facade.setCompanyProperties(compProperties);
        this.repository = new OCFutureAppointmentsRepository();
        this.facade.setFutureRespository(repository);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public static Appointment getRandomAppointment(boolean inThePast, long howManyDays) {
        final Random random = new Random();
        final int millisInDay = 24 * 60 * 60 * 1000;
        final String[] types = {"massage", "waxing", "pedicure", "manicure"};
        Appointment dummy = new Appointment();
        if (inThePast) {
            dummy.setDate(localToSqlDate(LocalDate.now().minusDays(howManyDays)));
        } else {
            dummy.setDate(localToSqlDate(LocalDate.now().plusDays(howManyDays)));
        }
        //set random time
        dummy.setPast(false);
        dummy.setTime(new Time((long) random.nextInt(millisInDay)));
        dummy.setNoShow(false);
        dummy.setClientName(randomAlphabetic(8));
        dummy.setEmail(randomAlphanumeric(10) + "@" + randomAlphanumeric(5) + ".ca");
        dummy.setType(types[random.nextInt(types.length)]);
        return dummy;
    }

    private static String getJsonOfAnObject(Object obj) {
        final ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "error";
        try {
            jsonInString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(PastAppointmentsTimerFacadeTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonInString;
    }

    @Test
    public void updatePastAppointmentsTest() {
        //get a random appointment so it is set as passed & print it
        Appointment dummy = getRandomAppointment(true, compProperties.getDaysBeforeMarkingAsPast() + 1);
        System.out.println(getJsonOfAnObject(dummy));
        //persist it
        repository.add(dummy);
        //suppose to mark it (as well as all the rest as passed with the facade's
        //job now
        facade.updatePastAppointments();
        assertTrue("We shouldn't have any ongoing appointments "
                + "seven days before", repository.getMarkedOngoingAndBeforeDate(
                        localToSqlDate(LocalDate.now().minusDays(compProperties.getDaysBeforeMarkingAsPast())))
                .isEmpty());
    }

    @Test
    public void deleteArchaicAppointmentsTest() {
        System.out.println("\ndeleteArchaicAppointmentsTest test:");
        compProperties.setDaysBeforeForceDeletingTheAppointmentRecord(150, null);
        Appointment dummy = getRandomAppointment(true, 180);
        System.out.println(getJsonOfAnObject(dummy));
        repository.add(dummy);
        facade.deleteArchaicAppointments();
        assertTrue("We should have 0 appointments before the corresponding flag "
                + "that is set in the CompanyProperties.", repository.getAll()
                .stream().filter(a -> a.getDate().before(localToSqlDate(
                                        LocalDate.now().minusDays(compProperties
                                                .getDaysBeforeForceDeletingTheAppointmentRecord()))))
                .collect(toList()).isEmpty());
    }
}
