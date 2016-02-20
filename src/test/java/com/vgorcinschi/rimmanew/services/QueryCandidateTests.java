/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidate;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidatesTriage;
import static com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QuerySignature.getByDate;
import static com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QuerySignature.getByDateAndTime;
import static com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QuerySignature.getByDateAndType;
import static com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QuerySignature.getByName;
import static com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QuerySignature.getByType;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class QueryCandidateTests {
    Optional<AppointmentsQueryCandidate> winner;
    
    public QueryCandidateTests() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getByDateAndTimeTest(){
        winner = new AppointmentsQueryCandidatesTriage("2015-11-21", 
                        "15:00", "massage", "Varvara").triage();
        System.out.println("getByDateAndTime: "+winner.get().getParams());
        assertTrue(winner.get().getSignature()==getByDateAndTime);
    }
    
    @Test
    public void getByDateAndTypeTest(){
        winner = new AppointmentsQueryCandidatesTriage("2015-11-21", 
                        null, "massage", "Varvara").triage();
        System.out.println("getByDateAndType: "+winner.get().getParams());
        assertTrue(winner.get().getSignature()==getByDateAndType);
    }
    
    @Test
    public void getByNameTest(){
        winner = new AppointmentsQueryCandidatesTriage("2015-11-21", 
                        null, null, "Varvara").triage();
        System.out.println("getByName: "+winner.get().getParams());
        assertTrue(winner.get().getSignature()==getByName);
    }
    
    @Test
    public void getByDateTest(){
        winner = new AppointmentsQueryCandidatesTriage("2015-11-21", 
                        null, null, null).triage();
        System.out.println("getByDate: "+winner.get().getParams());
        assertTrue(winner.get().getSignature()==getByDate);
    }
    
    @Test
    public void getByTypeTest(){
        winner = new AppointmentsQueryCandidatesTriage(null, 
                        null, "massage", null).triage();
        System.out.println("getByType: "+winner.get().getParams());
        assertTrue(winner.get().getSignature()==getByType);
    }
    
    @Test
    public void noParamsProvidedInTriageTest(){
        winner = new AppointmentsQueryCandidatesTriage(null, 
                        null, null, null).triage();
        assertFalse(winner.isPresent());
    }
}
