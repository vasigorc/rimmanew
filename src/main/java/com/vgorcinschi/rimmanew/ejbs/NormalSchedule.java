/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.DivizableDay;
import java.time.Duration;
import java.time.LocalTime;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;

/**
 *
 * @author vgorcinschi
 */
@Local
@Lock(LockType.WRITE)    
public interface NormalSchedule extends DivizableDay{
    void setStartAt(LocalTime lt);
    @Override
    LocalTime getStartAt();
    void setEndAt(LocalTime lt);
    @Override
    LocalTime getEndAt();
    void setDuration(Duration d);
    @Override
    Duration getDuration();
    void setBreakStart(LocalTime lt);
    @Override
    LocalTime getBreakStart();
    void setBreakEnd(LocalTime lt);
    @Override
    LocalTime getBreakEnd();
}
