/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.rest.services.SpecialDayResourceService;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class SpecialDayCreateTests {

    private SpecialDayResourceService service;

    public SpecialDayCreateTests() {
        this.service = new SpecialDayResourceService();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void blockedDayRegistered() {
        SpecialDay sd = service.checkAndBuild("2016-03-14", null, null, null, null, null, "true", null);
        assertNotNull(sd);
        System.out.println("\nblockedDayRegistered results:\n\n"
                + sd.getDate() + "\n" + sd.isBlocked());
    }

    @Test
    public void dayWithoutBreaksRegistered() {
        //is blocked must be false for this
        SpecialDay sd = service.checkAndBuild("2016-03-14", "9:00", "13:00", null, null, "40", "false", null);
        assertNotNull(sd);
        System.out.println("\ndayWithoutBreaksRegistered results:\n\n"
                + sd.getDate() + "\n" + sd.isBlocked() + "\n" + sd.getStartAt()
                + "\n" + sd.getEndAt());
    }
    
    @Test
    public void dayWithBreaksRegistered(){
        SpecialDay sd = service.checkAndBuild("2016-03-14", "9:00", "16:00", "12:00", "12:30", "40", "false", "This is a full special day!");
        assertNotNull(sd);
        System.out.println("\ndayWithBreaksRegistered results:\n\n"
                + sd.getDate() + "\n" + sd.isBlocked() + "\n" + sd.getStartAt()
                + "\n" + sd.getEndAt()+ "\n" + sd.getBreakStart()
        + "\n" + sd.getBreakEnd()+ "\n" + sd.getDuration()+ "\n" + sd.getMessage());
    }
}
