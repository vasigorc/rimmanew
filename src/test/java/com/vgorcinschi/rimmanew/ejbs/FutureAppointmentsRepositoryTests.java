/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static java.util.Optional.of;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class FutureAppointmentsRepositoryTests {

    private final OCFutureAppointmentsRepository futureRepository;
    private Date today;
    private final String aName, aType;

    public FutureAppointmentsRepositoryTests() {
        this.futureRepository = new OCFutureAppointmentsRepository();
        this.aName = "Rimma";
        this.aType = "waxing";
    }

    @Before
    public void setUp() {
        today = Java8Toolkit.localToSqlDate(LocalDate.now());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void obtainNotPastAndBeforeDateTest() {
        Optional<List<Appointment>> uncorrectedAppointments = of(futureRepository.getMarkedOngoingAndBeforeDate(today));
        assertThat(uncorrectedAppointments.get(), Matchers.instanceOf(List.class));
        System.out.println("\nobtainNotPastAndBeforeDateTest:\n"
                + "The size of the returned list is " + uncorrectedAppointments.get().size());
        System.out.println("\nThe appointments are:\n\nDate      Type     Is Passed\n"
                + "----------------------------------");
        uncorrectedAppointments.get().stream().forEach(a -> {
            System.out.println(
                    a.getDate() + " " + a.getType() + " " + a.isPast());
        });
    }

    @Test
    public void obtainCurrentAppointmentsTest() {
        Optional<List<Appointment>> futureApps = of(futureRepository.getAll());
        assertThat(futureApps.get(), Matchers.instanceOf(List.class));
        System.out.println("\nobtainCurrentAppointmentsTest:\n"
                + "The size of the returned list is " + futureApps.get().size());
        System.out.println("\nThe appointments are:\n\nDate      Type     Is Passed\n"
                + "----------------------------------");
        futureApps.get().stream().forEach(a -> {
            System.out.println(
                    a.getDate() + " " + a.getType() + " " + a.isPast());
        });
    }

    @Test
    public void obtainCurrentByNameTest() {
        Optional<List<Appointment>> futureApps = of(futureRepository.getByName(aName));
        assertThat("We should only get appointments for " + aName, futureApps.get(), Matchers.instanceOf(List.class));
        System.out.println("\nobtainCurrentByNameTest:\n"
                + "The size of the returned list is " + futureApps.get().size());
        System.out.println("\nThe appointments are:\n\nDate      Type     Is Passed    Name\n"
                + "----------------------------------");
        futureApps.get().stream().forEach(a -> {
            System.out.println(
                    a.getDate() + " " + a.getType() + " " + a.isPast() + " " + a.getClientName());
        });
    }

    @Test
    public void obtainCurrentByTypeTest() {
        Optional<List<Appointment>> futureApps = of(futureRepository.getByType(aType));
        assertThat("We should only get " + aType + " appointments.", futureApps.get(), Matchers.instanceOf(List.class));
        System.out.println("\nobtainCurrentByTypeTest:\n"
                + "The size of the returned list is " + futureApps.get().size());
        System.out.println("\nThe appointments are:\n\nDate      Type     Is Passed\n"
                + "----------------------------------");
        futureApps.get().stream().forEach(a -> {
            System.out.println(
                    a.getDate() + " " + a.getType() + " " + a.isPast());
        });
    }

    @Test
    public void markAsPassedAllAppointmentsWeekOldTest() {
        Date oneWeekBefore = Java8Toolkit.localToSqlDate(LocalDate.now().minusWeeks(1));
        int affectedRows = futureRepository.batchSetIsPassedStatus(oneWeekBefore);
        System.out.println(""
                + "\nmarkAsPassedAllAppointmentsWeekOldTest:\nA total of "+affectedRows+" was affected.");
        Assert.assertTrue(futureRepository.getMarkedOngoingAndBeforeDate(oneWeekBefore).isEmpty());
    }
}
