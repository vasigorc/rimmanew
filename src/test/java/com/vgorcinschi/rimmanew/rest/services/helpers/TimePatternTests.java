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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author vgorcinschi
 */
@RunWith(Parameterized.class)
public class TimePatternTests {
    
    private final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]"
            + "(:[0-5][0-9])*";
    private final String testableVar;
    
    public TimePatternTests(String var) {
        this.testableVar = var;
    }
    
    @Parameterized.Parameters
    public static Collection<String> getTestParameters(){
        LinkedList<String> params = new LinkedList<>();
        params.add("15:16");
        params.add("00:01");
        params.add("23:28");
        params.add("15:16:59");
        return params;
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testTimePattern(){
        assertTrue(testableVar.matches(TIME24HOURS_PATTERN));
    }
}
