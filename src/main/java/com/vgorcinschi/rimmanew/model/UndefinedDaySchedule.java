/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author vgorcinschi
 */
public class UndefinedDaySchedule implements ScheduleDay{

    public UndefinedDaySchedule() {
    }
    
    @Override
    public boolean isBlocked() {
        return false;
    }

    @Override
    public Optional<List<LocalTime>> getSlots() {
        return Optional.empty();
    }

    @Override
    public boolean isUndefined() {
        return true;
    }

    @Override
    public String getMessage() {
        return "";
    }
    
}
