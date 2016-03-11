/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.ejbs.SpecialDayRepository;
import com.vgorcinschi.rimmanew.rest.services.SpecialDayResourceService;
import javax.ws.rs.core.Response;
import javax.ws.rs.BadRequestException;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class SpecialDayResourceGetTests {

    private final SpecialDayResourceService service;
    private SpecialDayRepository repository;

    public SpecialDayResourceGetTests() {
        service = new SpecialDayResourceService();
        service.setRepository(new OutsideContainerSpecialDayRepository());

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    //tests for getSpecialDay method
    @Test
    public void nullPassedAsMethodAttribute() {
        try {
            service.getSpecialDay(null);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("You haven't provided a date"));
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void unacceptedDateArgument() {
        try {
            service.getSpecialDay("Poslezavtra");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("is not an accepted date format"));
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void successfullRequest() {
        Response response = service.getSpecialDay("2016-01-19");
        assertNotNull(response);
        System.out.println("Successfull response: " + response.getEntity());
    }

    @Test
    public void inexistantDateRequest() {
        try {
            service.getSpecialDay("2016-01-01");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Currently there is no special schedule"));
            System.out.println(e.getMessage());
        }
    }

    @Test(expected = BadRequestException.class)
    public void getAllZeroRequested() {
        service.getSpecialDays(0, 0);
    }

    @Test
    public void successGetAllSDaysRequest() {
        Response response = service.getSpecialDays(0,2);
        assertNotNull(response);
        System.out.println("Successfull response: " + response.getEntity());
    }
}
