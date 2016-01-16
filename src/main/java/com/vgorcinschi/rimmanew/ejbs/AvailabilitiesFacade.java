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

/**
 *
 * @author vgorcinschi
 */
@Local
public interface AvailabilitiesFacade {
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 7)
    Optional<ScheduleDay> searchAvailabilities(LocalDate l);
}
