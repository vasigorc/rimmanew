/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.rest.services.AppointmentResourceService;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class EmptyJaxbAppointmentListWrapperTest {

    public EmptyJaxbAppointmentListWrapperTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() {
        JaxbAppointmentListWrapper response
                = new JaxbAppointmentListWrapperBuilder(0, 0,
                        0, new LinkedList<Appointment>()).compose();
        System.out.println("\nCurrent: " + response.getCurrent());
        System.out.println("Previous: " + response.getPrevious());
        System.out.println("First: " + response.getFirst());
        System.out.println("Next: " + response.getNext());
        System.out.println("Last: " + response.getLast());
        System.out.println("-----------------------------");
        System.out.println("Current load size: " + response.getReturnedSize());
        assertTrue(response.getCurrent().isEmpty());
    }

    @Test
    public void testWithDefaultSizeAndOffset() {
        JaxbAppointmentListWrapper response
                = new JaxbAppointmentListWrapperBuilder(0, 0,
                        0, new LinkedList<Appointment>()).compose();
        System.out.println("\nCurrent: " + response.getCurrent());
        System.out.println("Previous: " + response.getPrevious());
        System.out.println("First: " + response.getFirst());
        System.out.println("Next: " + response.getNext());
        System.out.println("Last: " + response.getLast());
        System.out.println("-----------------------------");
        System.out.println("Current load size: " + response.getReturnedSize());
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE.INDENT_OUTPUT);
        String output;
        try {
            output = mapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AppointmentResourceService.class.getName()).log(Level.SEVERE, null, ex);
            output = "Code error serializing the appointments that you have requested";
        }
        System.out.println("JSON view: "+output);
        assertTrue(response.getCurrent().isEmpty());
    }
}
