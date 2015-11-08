/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import static com.vgorcinschi.rimmanew.util.Localizer.getLocalizedLabel;
import java.time.LocalTime;
import javax.faces.context.FacesContext;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author vgorcinschi
 */
public class TestAppointmentLocalSpecificValues {

    private Appointment appointment;

    public TestAppointmentLocalSpecificValues() {
    }

    @Before
    public void setUp() {
        appointment = new Appointment();
        appointment.setType("massage");
        appointment.setTime(localToSqlTime(LocalTime.of(17, 20)));
    }

    @After
    public void tearDown() {
    }

    //if context is not initialized an exception is thrown
    //else the returned value should be null 
    @Test
    public void testLocalizableType() {
        try {
             assertNull(getLocalizedLabel(appointment.getType()));
        } catch (Exception e) {
            assertThat("should throw a NPE when context is not initialized", e, 
                    instanceOf(java.lang.NullPointerException.class));
        }        
    }

    @Test
    public void testLocalTimeIsReturned() {
        assertThat("the specific method should return an instance of LocalTime",
                appointment.getLocalTimeRepr(), instanceOf(LocalTime.class));
    }
}
