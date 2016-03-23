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
import static com.vgorcinschi.rimmanew.util.InputValidators.stringIsValidDate;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.util.concurrent.ThreadLocalRandom;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class SpecialDayCreateTests {

    private final SpecialDayResourceService service;
    private final SpecialDayRepository repository;
    private final AppointmentRepository appointmentsRepository;

    public SpecialDayCreateTests() {
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
    public void blockedDayRegistered() {
        SpecialDay sd = service.checkAndBuild(Java8Toolkit
                .localToSqlDate(of(2016, 03, 14)), null, null, null,
                null, null, "true", null);
        assertNotNull(sd);
        System.out.println("\nblockedDayRegistered results:\n\n"
                + sd.getDate() + "\n" + sd.isBlocked());
    }

    @Test
    public void scheduleEndBeforeStartTest() {
        try {
            SpecialDay sd = service.checkAndBuild(Java8Toolkit
                    .localToSqlDate(of(2016, 03, 15)), "10:00", "9:00",
                    null, null, null, "false", null);
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(BadRequestException.class));
            System.out.println("\nscheduleEndBeforeStartTest: " + e.getMessage());
        }
    }

    @Test
    public void dayWithoutBreaksRegistered() {
        //is blocked must be false for this
        SpecialDay sd = service.checkAndBuild(Java8Toolkit
                .localToSqlDate(of(2016, 03, 14)), "9:00", "13:00",
                null, null, "40", "false", null);
        assertNotNull(sd);
        System.out.println("\ndayWithoutBreaksRegistered results:\n\n"
                + sd.getDate() + "\n" + sd.isBlocked() + "\n" + sd.getStartAt()
                + "\n" + sd.getEndAt());
    }

    @Test
    public void dayWithBreaksRegistered() {
        SpecialDay sd = service.checkAndBuild(Java8Toolkit
                .localToSqlDate(of(2016, 03, 14)), "9:00", "16:00",
                "12:00", "12:30", "40", "false", "This is a full special day!");
        assertNotNull(sd);
        System.out.println("\ndayWithBreaksRegistered results:\n\n"
                + sd.getDate() + "\n" + sd.isBlocked() + "\n" + sd.getStartAt()
                + "\n" + sd.getEndAt() + "\n" + sd.getBreakStart()
                + "\n" + sd.getBreakEnd() + "\n" + sd.getDuration() + "\n" + sd.getMessage());
    }

    @Test
    public void breakEndBeforeBreakStartTest() {
        try {
            SpecialDay sd = service.checkAndBuild(Java8Toolkit
                    .localToSqlDate(of(2016, 03, 14)), "9:00", "15:00",
                    "12:30", "12:00", "40", "false", null);
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(BadRequestException.class));
            System.out.println("\nbreakEndBeforeBreakStartTest: " + e.getMessage());
        }
    }

    @Test
    public void forgotToProvideDateTest() {
        try {
            service.addSpecialDay(null, "9:00", "15:00",
                    "12:30", "12:00", "40", "false", null, "false");
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(BadRequestException.class));
            System.out.println("\nforgotToProvideDateTest: " + e.getMessage());
        }
    }

    @Test
    public void wrongDurationFormat() {
        try {
            SpecialDay sd = service.checkAndBuild(Java8Toolkit.localToSqlDate(of(2016, 03, 15)), "9:00", "15:00",
                    "12:00", "12:30", "half an hour", "false", null);
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(BadRequestException.class));
            System.out.println("\nwrongDurationFormat: " + e.getMessage());
        }
    }

    @Test
    public void nullDurationSentTest() {
        try {
            SpecialDay sd = service.checkAndBuild(Java8Toolkit
                    .localToSqlDate(of(2016, 03, 15)), "9:00", "15:00",
                    "12:00", "12:30", null, "false", "Short day");
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(BadRequestException.class));
            System.out.println("\nnullDurationSentTest: " + e.getMessage());
        }
    }

    @Test
    public void emptyStringDuration() {
        try {
            SpecialDay sd = service.checkAndBuild(Java8Toolkit
                    .localToSqlDate(of(2016, 03, 15)), "9:00", "15:00",
                    "12:00", "12:30", "", "false", "Short day");
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(BadRequestException.class));
            System.out.println("\nemptyStringDuration: " + e.getMessage());
        }
    }

    //INTEGRATION TESTS START HERE
    @Test
    @Ignore
    public void successfullyStoredSpecialDay() {
        String dayRepr = null;
        while (!stringIsValidDate.apply(dayRepr)) {
            dayRepr = getRandomStringDate();
        }
        Response response = service.addSpecialDay(dayRepr, "9:00", "15:00",
                "12:00", "12:30", "30", "false", "Short day", "true");
        System.out.println("\nsuccessfullyStoredSpecialDayJSON: " + response.getEntity().toString());
        assertTrue("We are "
                + " testing whether the URI contains the"
                + " start_at parameter", response.getEntity().toString().contains("specialdays"));
    }

    @Test
    public void tryToStoreInvalidDate() {
        try {
            Response response = service.addSpecialDay("2016-02-30", "9:00", "15:00",
                    "12:00", "12:30", "30", "false", "Short day", "true");
        } catch (Exception e) {
            assertThat("Since there isn't a 30th February"
                    + " we are expecting a 400 Error.", e, instanceOf(BadRequestException.class));
            System.out.println("\n tryToStoreInvalidDate: " + e.getMessage());
        }
    }

    @Test
    public void savingSpecialWithConflicts() {
        try {
            Response response = service.addSpecialDay("2016-03-31", "9:00", "15:00",
                    "12:00", "12:30", "30", "false", "Short day", "false");
        } catch (Exception e) {
            assertThat("Since there isn't a 30th February"
                    + " we are expecting a 400 Error.", e, instanceOf(BadRequestException.class));
            System.out.println("\nsavingSpecialWithConflicts: " + e.getMessage());
        }
    }

    public String getRandomStringDate() {
        long minDay = LocalDate.of(2016, 3, 1).toEpochDay();
        long maxDay = LocalDate.of(2016, 6, 30).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.toString();
    }
}
