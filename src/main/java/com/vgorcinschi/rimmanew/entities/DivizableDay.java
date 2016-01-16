/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import java.time.Duration;
import java.time.LocalTime;

/**
 *this should be a common interface to be 
 * both extended by NormalSchedule and implemented by SpecialDayDivizableAdapter
 * class
 * it should return one of these implementations, but 
 * by only providing get methods for either of them
 * @author vgorcinschi
 */
public interface DivizableDay {
    LocalTime getStartAt();
    LocalTime getEndAt();
    Duration getDuration();   
    LocalTime getBreakStart();
    LocalTime getBreakEnd();
}
