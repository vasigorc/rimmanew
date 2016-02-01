/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.util.Collection;
import java.util.LinkedList;
import org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author vgorcinschi
 */
@RunWith(Parameterized.class)
public class DatePatternTests {
    
    private final String dateExample;
    
    public DatePatternTests(String aDate) {
        this.dateExample = aDate;
    }
    
    @Parameters
    public static Collection<String> getTestParameters(){
        LinkedList<String> params = new LinkedList<>();
        params.add("2015-06-03");
        params.add("1999-06-23");
        params.add("2016-01-01");
        params.add("9999-99-99");
        params.add("Not-a-date");
        return params;
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testDatePattern(){
        assertTrue(dateExample.matches("\\d{4}-\\d{2}-\\d{2}"));
    }
}
