/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import java.time.Duration;
import java.time.LocalTime;
import javax.ejb.Local;

/**
 *
 * @author vgorcinschi
 */
@Local
public interface NormalSchedule {
    void setDayStart(LocalTime lt);
    LocalTime getDayStart();
    void setDayEnd(LocalTime lt);
    LocalTime getDayEnd();
    void setDuration(Duration d);
    Duration getDuration();
    void setBreakStart(LocalTime lt);
    LocalTime getBreakStart();
}
