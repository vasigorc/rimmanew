/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author vgorcinschi
 */
public class ClosedDaySchedule implements ScheduleDay{

    private String message;
    
    @Override
    public boolean isBlocked() {
        return true;
    }

    @Override
    public Optional<List<LocalTime>> getSlots() {
        return Optional.of(new LinkedList<LocalTime>());
    }

    @Override
    public boolean isUndefined() {
        return false;
    }

    @Override
    public String getMessage() {
        return Optional.ofNullable(message).orElse("");
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
