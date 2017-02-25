/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.services.OutsideContainerSpecialDayRepository;
import static java.time.LocalDate.parse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class SpecialDayRepoGetTests {
    SpecialDayRepository repository;
    
    public SpecialDayRepoGetTests() {
        repository = new OutsideContainerSpecialDayRepository();
    }

    @Test
    public void getAllSpecialDaysTest(){
        int size = repository.getAll().size();
        assertTrue(size>1);
        System.out.println("Current number of Special Schedule Days is "+size); 
    }
    
    @Test
    public void getOneSpecialDayTest(){
        SpecialDay day = repository.getSpecialDay(parse("2016-02-01"));
        assertNotNull("We expect this to not be null, since "
                + "the date was looked-up in the DB upfront.", day);
        System.out.println(day.getDate());
    }
}
