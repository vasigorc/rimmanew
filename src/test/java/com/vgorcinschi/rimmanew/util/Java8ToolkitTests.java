/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class Java8ToolkitTests {

    public Java8ToolkitTests() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testDurationSplitr() {
        long start = System.nanoTime();
        List<LocalTime> test
                = Java8Toolkit.durationSplitr(LocalTime.of(9, 0),
                        LocalTime.of(10, 30), Duration.ofMinutes(30));
        long duration = (System.nanoTime() - start) / 1_000_000;
        assertEquals(3, test.size());
        System.out.println("While loop result returned: " + test + "\nResult returned in " + duration + " msecs\n");
    }

    @Test
    public void testRecursiveDurationSplitr() {
        long start = System.nanoTime();
        List<LocalTime> test = Java8Toolkit.recursiveDurationSplitr(
                Duration.ofMinutes(30), LocalTime.of(9, 0), LocalTime.of(10, 30));
        long duration = (System.nanoTime() - start) / 1_000_000;
        assertEquals(3, test.size());
        System.out.println("Recursive result is: " + test + "\nResult returned in " +duration+ " msecs\n");
    } 
}
