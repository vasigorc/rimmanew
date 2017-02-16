/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import static org.hamcrest.Matchers.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author vgorcinschi
 */
@RunWith(Parameterized.class)
public class LocalToSQLConverterTest {

    private final LocalTime localTime;

    public LocalToSQLConverterTest(LocalTime localTime) {
        this.localTime = localTime;
    }

    @Parameters
    public static Collection<LocalTime> getTestParameters() {
        ArrayList<LocalTime> times = new ArrayList<>();
        times.add(LocalTime.MIN);
        times.add(LocalTime.NOON);
        times.add(LocalTime.of(9, 56));
        times.add(LocalTime.now());
        times.add(LocalTime.of(21, 13));
        return times;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testThatTheMethodAlwaysReturnsATimeInstance() {
        assertThat("The object returned by the static method "
                + "must be an instance of the class "
                + "java.sql.Time - this is crucial for our persistence", 
                localToSqlTime(localTime), instanceOf(java.sql.Time.class));
        System.out.println("Tested for: "+localToSqlTime(localTime).toLocalTime());
    }
    
}
