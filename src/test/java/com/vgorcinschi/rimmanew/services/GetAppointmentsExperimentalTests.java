/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidate;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidatesTriage;
import com.vgorcinschi.rimmanew.util.ExecutorFactoryProvider;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
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
public class GetAppointmentsExperimentalTests {

    private String appDate, appTime, appType, clientName;

    public GetAppointmentsExperimentalTests() {
    }

    @Before
    public void setUp() {
        appDate = "2016-03-05";
        appTime = "11:00";
        appType = "massage";
        clientName = "Elena";
    }

    @After
    public void tearDown() {
    }

    @Test
    public void takeOneTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will 
        //designate the main query that will be called from repository
        //as it is possible that none of the Appointment params are specified
        //the return type for the triage is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        System.out.println(triage.allProps());
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> objectMap = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                objectMap.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            objectMap.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            objectMap.put("date", dateConverted);
        }
        System.out.println(objectMap);
        assertThat(objectMap.get("time"), instanceOf(java.sql.Time.class));
        assertThat(objectMap.get("date"), instanceOf(java.sql.Date.class));
    }

    @Test
    public void timeInvalidFormat() {
        Time timeConverted = null;
        Date dateConverted = null;
        appTime = "";
        try {
            if (appTime != null && !appTime.equals("")) {
                timeConverted = new SqlTimeConverter().fromString(appTime);
            }
            if (appDate != null && !appDate.equals("")) {
                dateConverted = new SqlDateConverter().fromString(appDate);
            }
            //now we should be ready to call the triage class that will 
            //designate the main query that will be called from repository
            //as it is possible that none of the Appointment params are specified
            //the return type for the triage is Optional
            AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                    appTime, appType, clientName);
            System.out.println(triage.allProps());
            Optional<AppointmentsQueryCandidate> winner = triage.triage();
            //unverified map with all params as strings - may contain empty values
            Map<String, Object> stringMap = triage.allProps();
            //we will replace the strings with Date and Time objects for further calcs
            Map<String, Object> objectMap = new HashMap<>();
            //populate objectMap with non-empty strings only
            stringMap.forEach((k, v) -> {
                if (!v.equals("")) {
                    objectMap.put(k, v);
                }
            });
            if (ofNullable(timeConverted).isPresent()) {
                objectMap.put("time", timeConverted);
            }
            if (ofNullable(dateConverted).isPresent()) {
                objectMap.put("date", dateConverted);
            }
        } catch (Exception e) {
            assertThat(e, instanceOf(BadRequestException.class));
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void dateInvalidFormat() {
        Time timeConverted = null;
        Date dateConverted = null;
        appDate = "dataa";
        try {
            if (appTime != null && !appTime.equals("")) {
                timeConverted = new SqlTimeConverter().fromString(appTime);
            }
            if (appDate != null && !appDate.equals("")) {
                dateConverted = new SqlDateConverter().fromString(appDate);
            }
            //now we should be ready to call the triage class that will 
            //designate the main query that will be called from repository
            //as it is possible that none of the Appointment params are specified
            //the return type for the triage is Optional
            AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                    appTime, appType, clientName);
            System.out.println(triage.allProps());
            Optional<AppointmentsQueryCandidate> winner = triage.triage();
            //unverified map with all params as strings - may contain empty values
            Map<String, Object> stringMap = triage.allProps();
            //we will replace the strings with Date and Time objects for further calcs
            Map<String, Object> objectMap = new HashMap<>();
            //populate objectMap with non-empty strings only
            stringMap.forEach((k, v) -> {
                if (!v.equals("")) {
                    objectMap.put(k, v);
                }
            });
            if (ofNullable(timeConverted).isPresent()) {
                objectMap.put("time", timeConverted);
            }
            if (ofNullable(dateConverted).isPresent()) {
                objectMap.put("date", dateConverted);
            }
        } catch (Exception e) {
            assertThat(e, instanceOf(BadRequestException.class));
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void noParamsPassedTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        appDate = "";
        appTime = "";
        appType = "";
        clientName = "";
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will 
        //designate the main query that will be called from repository
        //as it is possible that none of the Appointment params are specified
        //the return type for the triage is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        System.out.println(triage.allProps());
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> objectMap = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                objectMap.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            objectMap.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            objectMap.put("date", dateConverted);
        }
        assertTrue("The object map should remain empty as"
                + " there were no params provided", objectMap.isEmpty());
    }

    @Test
    public void withCompletableFutureTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        CompletableFuture<AppointmentsQueryCandidatesTriage> future
                = CompletableFuture.supplyAsync(() -> {
                    return new AppointmentsQueryCandidatesTriage(appDate,
                            appTime, appType, clientName);
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will 
        //designate the main query that will be called from repository
        //as it is possible that none of the Appointment params are specified
        //the return type for the triage is Optional
        AppointmentsQueryCandidatesTriage triage;
        try {
            triage = future.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new InternalServerErrorException("Server took too long to"
                    + " response. Please try again later");
        }
        System.out.println(triage.allProps());
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> objectMap = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                objectMap.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            objectMap.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            objectMap.put("date", dateConverted);
        }
        assertTrue(objectMap.size()==4);
    }
    
    @Test(expected = BadRequestException.class)
    public void cancelCompletableFutureOnOtherExceptionTest(){
        Time timeConverted = null;
        Date dateConverted = null;
        appDate = "date";
        appTime = "";
        appType = "";
        clientName = "";
        CompletableFuture<AppointmentsQueryCandidatesTriage> future
                = CompletableFuture.supplyAsync(() -> {
                    return new AppointmentsQueryCandidatesTriage(appDate,
                            appTime, appType, clientName);
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        /*
            CompletableFuture should be terminated within any if
        */
        if (appTime != null && !appTime.equals("")) {
            System.out.println("Before -is completable Future cancelled: "+future.isCancelled());
            timeConverted = new SqlTimeConverter(future).fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            System.out.println("Before - is completable Future cancelled: "+future.isCancelled());
            dateConverted = new SqlDateConverter(future).fromString(appDate);
        }
        //now we should be ready to call the triage class that will 
        //designate the main query that will be called from repository
        //as it is possible that none of the Appointment params are specified
        //the return type for the triage is Optional
        AppointmentsQueryCandidatesTriage triage;
        try {
            triage = future.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new InternalServerErrorException("Server took too long to"
                    + " response. Please try again later");
        }
        System.out.println(triage.allProps());
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> objectMap = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                objectMap.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            objectMap.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            objectMap.put("date", dateConverted);
        }
    }
}
