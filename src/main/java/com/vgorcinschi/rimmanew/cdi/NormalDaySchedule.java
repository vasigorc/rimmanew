/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author vgorcinschi
 */
public class NormalDaySchedule implements ScheduleDay {
    
    private final Optional<List<LocalTime>> slots;

    public NormalDaySchedule(Optional<List<LocalTime>> slots) {
        this.slots = slots;
    }    

    @Override
    public boolean isBlocked() {
        return false;
    }

    @Override
    public Optional<List<LocalTime>> getSlots() {
        return slots;
    }

    @Override
    public boolean isUndefined() {
        return false;
    }

    @Override
    public String getMessage() {
        return "";
    }
    
}
