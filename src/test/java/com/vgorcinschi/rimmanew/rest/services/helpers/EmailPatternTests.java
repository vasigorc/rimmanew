/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.util.Collection;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.vgorcinschi.rimmanew.util.InputValidators.EMAIL_PATTERN;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author vgorcinschi
 */
@RunWith(Parameterized.class)
public class EmailPatternTests {
    
    private final String candidate;
    
    public EmailPatternTests(String candidate) {
        this.candidate=candidate;
    }
    
    @Parameterized.Parameters
    public static Collection<String> getTestParameters(){
        LinkedList<String> params = new LinkedList<>();
        params.add("someemail@adas.se");
        params.add("123asdas@123sd.asd");
        params.add("elena@mail.md");
        params.add("mail@gov.qc.ca");
        params.add("ridiculousemail@dm");
        return params;
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testEmailPattern(){
         assertTrue(candidate.matches(EMAIL_PATTERN));
    }
}