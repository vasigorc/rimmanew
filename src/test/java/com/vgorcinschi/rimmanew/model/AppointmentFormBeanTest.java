package com.vgorcinschi.rimmanew.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author vgorcinschi
 */
public class AppointmentFormBeanTest {
    
    private Mockery context = new JUnit4Mockery();
    private Date mockRequest;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

     @Test
     @Ignore("We need to implement the native bean first")
     public void testThePassedDateSaved() throws ParseException {         
     }
}
