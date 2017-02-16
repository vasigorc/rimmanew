/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.ejbs.OCFutureAppointmentsRepository;
import com.vgorcinschi.rimmanew.ejbs.OutsideContainerJpaTests;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.rest.services.AppointmentResourceService;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidate;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidatesTriage;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QueryCommandControl;
import com.vgorcinschi.rimmanew.util.ExecutorFactoryProvider;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
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
public class GetAppointmentsExperimentalTests {

    private String appDate, appTime, appType, clientName;
    private final AppointmentRepository repository;
    private final AppointmentResourceService service;

    public GetAppointmentsExperimentalTests() {
        OutsideContainerJpaTests tests = new OutsideContainerJpaTests();
        this.repository = tests.getRepository();
        this.service = new AppointmentResourceService();
        this.service.setRepository(repository);
        this.service.setFutureRepository(new OCFutureAppointmentsRepository());
    }

    @Before
    public void setUp() {
        appDate = "2016-02-19";
        appTime = "10:00";
        appType = "massage";
        clientName = "Egzbeta";
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
    public void experimentalScenarioTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        System.out.println("experimentalScenarioTest\n"
                + "Winner is: " + winner.get().getSignature());
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        CompletableFuture<List<Appointment>> futureList = supplyAsync(
                () -> {
                    if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        Set<String> unusedKeys = checkedParameters.keySet();
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        try {
            //if the list.size() ==0 return a corresponding Response
            //do the forEach on checkedParameters to filter futureList.stream()
            //with the remaining keys of checkedParameters
            //collect toList() and proceed with th rest of the code
            initialSelection = futureList.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        assertFalse(initialSelection.isEmpty());
        System.out.println("Returned list: ");
        initialSelection.stream().forEach((a) -> {
            System.out.println(a.getClientName());
        });
    }

    @Test(expected = InternalServerErrorException.class)
    public void timeoutMock() {
        Time timeConverted = null;
        Date dateConverted = null;
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        System.out.println("experimentalScenarioTest\n"
                + "Winner is: " + winner.get().getSignature());
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        CompletableFuture<List<Appointment>> futureList = supplyAsync(
                () -> {
                    if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        Set<String> unusedKeys = checkedParameters.keySet();
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        try {
            //for the purpose of this test we'll just wait 2 nanoseconds
            //to force InternalServerErrorException
            initialSelection = futureList.get(2, TimeUnit.NANOSECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        assertFalse(initialSelection.isEmpty());
        System.out.println("Returned list: ");
        initialSelection.stream().forEach((a) -> {
            System.out.println(a.getClientName());
        });
    }

    @Test
    public void getAllIsCalledWhenNoParamsAreCalled() {
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
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        System.out.println("experimentalScenarioTest\n"
                + "Winner is returned: " + winner.isPresent());
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        CompletableFuture<List<Appointment>> futureList = supplyAsync(
                () -> {
                    if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        Set<String> unusedKeys = checkedParameters.keySet();
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        try {
            //if the list.size() ==0 return a corresponding Response
            //do the forEach on checkedParameters to filter futureList.stream()
            //with the remaining keys of checkedParameters
            //collect toList() and proceed with th rest of the code
            initialSelection = futureList.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        assertTrue(initialSelection.size() > 10);
        System.out.println("All list expected: ");
        initialSelection.stream().forEach((a) -> {
            System.out.println(a.getClientName());
        });
    }

    @Test
    public void getByDateTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        appDate = "2016-01-22";
        appTime = "";
        appType = "";
        clientName = "";
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        CompletableFuture<List<Appointment>> futureList = supplyAsync(
                () -> {
                    if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        Set<String> unusedKeys = checkedParameters.keySet();
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        try {
            //if the list.size() ==0 return a corresponding Response
            //do the forEach on checkedParameters to filter futureList.stream()
            //with the remaining keys of checkedParameters
            //collect toList() and proceed with th rest of the code
            initialSelection = futureList.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        assertTrue(initialSelection.stream().allMatch((a) -> a.getDate().toString().equalsIgnoreCase("2016-01-22")));
        System.out.println("Only one date is expected here: ");
        initialSelection.stream().forEach((a) -> {
            System.out.println(a.getDate());
        });
    }

    @Test
    public void getByDateAndTypeTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        appDate = "2016-02-19";
        appTime = "";
        appType = "manicure";
        clientName = "";
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        System.out.println("getByDateAndTypeTest\n Query type:" + winner.get().getSignature());
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        CompletableFuture<List<Appointment>> futureList = supplyAsync(
                () -> {
                    if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        Set<String> unusedKeys = checkedParameters.keySet();
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        try {
            //if the list.size() ==0 return a corresponding Response
            //do the forEach on checkedParameters to filter futureList.stream()
            //with the remaining keys of checkedParameters
            //collect toList() and proceed with th rest of the code
            initialSelection = futureList.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        assertTrue(initialSelection.size() == 1);
        System.out.println("Only one appointment is expected here: ");
        initialSelection.stream().forEach((a) -> {
            System.out.println(a.getDate());
        });
    }

    @Test
    public void getByDateTypeTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        appDate = "";
        appTime = "";
        appType = "manicure";
        clientName = "";
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        System.out.println("getByTypeTest\nQuery type:" + winner.get().getSignature());
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        CompletableFuture<List<Appointment>> futureList = supplyAsync(
                () -> {
                    if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        Set<String> unusedKeys = checkedParameters.keySet();
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        try {
            //if the list.size() ==0 return a corresponding Response
            //do the forEach on checkedParameters to filter futureList.stream()
            //with the remaining keys of checkedParameters
            //collect toList() and proceed with th rest of the code
            initialSelection = futureList.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        assertTrue(initialSelection.stream().allMatch((a) -> a.getType().equals("manicure")));
        System.out.println("Only one appointment type is expected here: ");
        initialSelection.stream().forEach((a) -> {
            System.out.println(a.getType());
        });
    }

    @Test
    public void getByDateTypeWithSuccessiveFuturesTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        appDate = "";
        appTime = "";
        appType = "manicure";
        clientName = "";
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                            appTime, appType, clientName);
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        //In addition - we will need some more stuff performed in between, so
        //we wll call it asynchroinously
        CompletableFuture<Optional<AppointmentsQueryCandidate>> futureWinner
                = CompletableFuture.supplyAsync(() -> {                    
                    Optional<AppointmentsQueryCandidate> winner = triage.triage();
                    return winner;
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        //but eventually we need a list of appointments and not a type of query
        //so let us extend our asynchronous call
        CompletableFuture<List<Appointment>> futureList = futureWinner.thenApplyAsync((winner)->{
            if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
        });        
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        
        Set<String> unusedKeys = checkedParameters.keySet();
        Optional<AppointmentsQueryCandidate> winner = Optional.empty();
        try {
            winner = futureWinner.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        try {
            //if the list.size() ==0 return a corresponding Response
            //do the forEach on checkedParameters to filter futureList.stream()
            //with the remaining keys of checkedParameters
            //collect toList() and proceed with th rest of the code
            initialSelection = futureList.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        assertTrue(initialSelection.stream().allMatch((a) -> a.getType().equals("manicure")));
        System.out.println("Only one appointment type is expected here: ");
        initialSelection.stream().forEach((a) -> {
            System.out.println(a.getType());
        });
    }
    
    @Test(expected = InternalServerErrorException.class)
    public void FirstOfTheSuccessiveFuturesInterruptedExceptionTest() {
        Time timeConverted = null;
        Date dateConverted = null;
        appDate = "";
        appTime = "";
        appType = "manicure";
        clientName = "";
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                            appTime, appType, clientName);
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        //In addition - we will need some more stuff performed in between, so
        //we wll call it asynchroinously
        CompletableFuture<Optional<AppointmentsQueryCandidate>> futureWinner
                = CompletableFuture.supplyAsync(() -> {                    
                    Optional<AppointmentsQueryCandidate> winner = triage.triage();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            }
                    return winner;
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        //but eventually we need a list of appointments and not a type of query
        //so let us extend our asynchronous call
        CompletableFuture<List<Appointment>> futureList = futureWinner.thenApply((winner)->{
            if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
        });        
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        
        Set<String> unusedKeys = checkedParameters.keySet();
        Optional<AppointmentsQueryCandidate> winner = Optional.empty();
        try {
            winner = futureWinner.get(1, TimeUnit.NANOSECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        try {
            //if the list.size() ==0 return a corresponding Response
            //do the forEach on checkedParameters to filter futureList.stream()
            //with the remaining keys of checkedParameters
            //collect toList() and proceed with th rest of the code
            initialSelection = futureList.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(GetAppointmentsExperimentalTests.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team;");
        }        
    }
    
    @Test
    public void requestEndsInEmptyResponseTest(){
        Response response = service.getAppointments("2019-02-12", "", "massage", "", 0, 10,
                "true", "false");
        assertTrue(response.hasEntity());
        System.out.println("Empty wrapper HTTP response in JSON: "+response.getEntity().toString());
    }
    
    @Test
    public void sizeValidatorEmptyListPassedTest(){
        assertEquals("We are passing here 0 for an empty "
                + "list size, 1 for the default offset "
                + "and 10 for the default request size",0, service.sizeValidator(0, 0, 10));
    }
    
    @Test
    public void requestSkippedAndLimitedToZeroTest(){
        Response response = service.getAppointments("2016-01-28", "13:00", "massage", "", 0, 10,
                "true", "false");
        assertTrue(response.hasEntity());
        System.out.println("requestSkippedAndLimitedToZeroTest JSON: "+response.getEntity().toString());
    }
}
