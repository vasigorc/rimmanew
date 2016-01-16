/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.SpecialDay;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import javax.ejb.AccessTimeout;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;

/**
 *
 * @author vgorcinschi
 */
@Local
@Lock(LockType.WRITE)
public interface SpecialDayRepository {

    //TODO Add SpecialDayService for the Admin part, where we will
    //need differentiation between update & add
    //Create
    boolean setSpecialDay(SpecialDay sd);
    //Read
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    SpecialDay getSpecialDay(LocalDate ld);
    //Update
    boolean updateSpecialDay(SpecialDay sd);
    //Delete
    boolean deleteSpecialDay(SpecialDay sd);
    
}
