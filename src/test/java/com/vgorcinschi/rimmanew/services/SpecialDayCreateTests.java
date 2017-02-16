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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class SpecialDayCreateTests {

    private final SpecialDayResourceService service;
    private final SpecialDayRepository repository;
    private final AppointmentRepository appointmentsRepository;
    private final Map<String, Object> configs = new HashMap<>(1);
    private final JsonBuilderFactory factory;

    public SpecialDayCreateTests() {
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
    public void successfullyStoredSpecialDay() {
        String dayRepr = null;
        while (!stringIsValidDate.apply(dayRepr)) {
            dayRepr = getRandomStringDate();
        }
        JsonObject value = factory.createObjectBuilder()
                .add("date", dayRepr)
                .add("startAt", "9:00")
                .add("endAt", "15:00")
                .add("breakStart", "12:00")
                .add("breakEnd", "12:30")
                .add("duration", "30")
                .add("blocked", "false")
                .add("message", "Short day")
                .add("allowConflicts", "true").build();
        InputStream is = new ByteArrayInputStream(value.toString().getBytes());
        Response response = service.addSpecialDay(is);
        System.out.println("\nsuccessfullyStoredSpecialDayJSON: " + response.getEntity().toString());
        assertTrue("We are "
                + " testing whether the URI contains the"
                + " start_at parameter", response.getEntity().toString().contains("specialdays"));
    }

    @Test
    public void forgotToProvideDateTest() {
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("date", "")
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:30")
                    .add("breakEnd", "12:00")
                    .add("duration", "40")
                    .add("blocked", "false")
                    .add("message", "")
                    .add("allowConflicts", "false").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.addSpecialDay(is);
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(BadRequestException.class));
            System.out.println("\nforgotToProvideDateTest: " + e.getMessage());
        }
    }

    @Test
    public void tryToStoreInvalidDate() {
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("date", "2016-02-30")
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "12:30")
                    .add("duration", "30")
                    .add("blocked", "false")
                    .add("message", "Short day")
                    .add("allowConflicts", "true").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.addSpecialDay(is);
        } catch (Exception e) {
            assertThat("Since there isn't a 30th February"
                    + " we are expecting a 400 Error.", e, instanceOf(BadRequestException.class));
            System.out.println("\ntryToStoreInvalidDate: " + e.getMessage());
        }
    }

    @Test
    public void savingSpecialWithConflicts() {
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("date", "2016-03-31")
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "12:30")
                    .add("duration", "30")
                    .add("blocked", "false")
                    .add("message", "Short day")
                    .add("allowConflicts", "false").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.addSpecialDay(is);
        } catch (Exception e) {
            assertThat(e, instanceOf(BadRequestException.class));
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

    @Test
    public void testEmptyDurationSubmission() {
        String dayRepr = null;
        while (!stringIsValidDate.apply(dayRepr)) {
            dayRepr = getRandomStringDate();
        }
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("date", dayRepr)
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "12:30")
                    .add("duration", "")
                    .add("blocked", "false")
                    .add("message", "Short day")
                    .add("allowConflicts", "false").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.addSpecialDay(is);
        } catch (Exception e) {
            assertThat("We are expecting a 400 Error because of the"
                    + "empty duration field.", e, instanceOf(BadRequestException.class));
            System.out.println("\ntestEmptyDurationSubmission: " + e.getMessage());
        }
    }

    @Test
    public void attemptingToStoreAnEngagedSpecialDay() {
        //we know that '2016-01-01' is in the database
        try {
            JsonObject value = factory.createObjectBuilder()
                    .add("date", "2016-01-01")
                    .add("startAt", "9:00")
                    .add("endAt", "15:00")
                    .add("breakStart", "12:00")
                    .add("breakEnd", "12:30")
                    .add("duration", "45")
                    .add("blocked", "false")
                    .add("message", "Short day")
                    .add("allowConflicts", "false").build();
            InputStream is = new ByteArrayInputStream(value.toString().getBytes());
            service.addSpecialDay(is);
        } catch (Exception e) {
            assertThat("We are expecting a 400 Error because of the"
                    + " 'occupied' day.", e, instanceOf(BadRequestException.class));
            System.out.println("\nattemptingToStoreAnEngagedSpecialDay: " + e.getMessage());
        }
    }
}
