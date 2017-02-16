/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.entities.SpecialDay;
import static com.vgorcinschi.rimmanew.util.InputValidators.allStringsAreGood;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.genericTypeIdentifier;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class Java8ToolkitTests {
    List<SpecialDay> days = new LinkedList<>();

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
    
    @Test
    public void retrieveTypeTest(){
        Field stringListField = null;
        try {
            stringListField = Java8ToolkitTests.class.getDeclaredField("days");
        } catch (NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(Java8ToolkitTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        assertEquals("com.vgorcinschi.rimmanew.entities.SpecialDay",stringListClass.getTypeName());
    }
    
    @Test
    public void retrieveTypeWithFunctionTest(){
        try {
            assertEquals("com.vgorcinschi.rimmanew.entities.SpecialDay",
                    genericTypeIdentifier.apply(Java8ToolkitTests.class.getDeclaredField("days")));
        } catch (NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(Java8ToolkitTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void allStringsAreGoodTest(){
        String [] valid = {"Monday", "Tuesday"};
        String [] invalidWithNull = {null, "Tuesday"};
        String [] invalidWithEmpty = {"Monday", ""};
        String [] empty = {};
        String [] bothEmpty = {"",""};
        String [] bothNull = {null,null};
        assertTrue(InputValidators.allStringsAreGood.apply(valid));
        assertFalse(InputValidators.allStringsAreGood.apply(invalidWithEmpty));
        assertFalse(InputValidators.allStringsAreGood.apply(invalidWithNull));
        assertFalse(InputValidators.allStringsAreGood.apply(empty));
        assertFalse(InputValidators.allStringsAreGood.apply(bothEmpty));
        assertFalse(InputValidators.allStringsAreGood.apply(bothNull));
    }
}
