/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.model.ScheduleDay;
import java.time.LocalDate;
import java.util.Optional;
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
public interface AbnormalSchedule {

    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    Optional<ScheduleDay> getUnavailableDay(LocalDate ld);

    boolean setUnavailableDay(ScheduleDay sd);

    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    Optional<ScheduleDay> getSpecialDay(LocalDate ld);

    boolean setSpecialDay(ScheduleDay sd);
}
