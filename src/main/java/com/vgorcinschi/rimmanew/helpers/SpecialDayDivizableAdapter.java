/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

import com.vgorcinschi.rimmanew.entities.DivizableDay;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import java.time.Duration;
import java.time.LocalTime;

/**
 *an adapter class for the SpecialDay entity
 * that basically transforms times to localtimes,
 * by implementing the DivizableDay interface
 * @author vgorcinschi
 */
public class SpecialDayDivizableAdapter implements DivizableDay{
    private final SpecialDay specialDay;

    public SpecialDayDivizableAdapter(SpecialDay specialDay) {
        this.specialDay = specialDay;
    }
    
    @Override
    public LocalTime getStartAt() {
        return specialDay.getStartAt().toLocalTime();
    }

    @Override
    public LocalTime getEndAt() {
        return specialDay.getEndAt().toLocalTime();
    }

    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(specialDay.getDuration());
    }

    @Override
    public LocalTime getBreakStart() {
        return specialDay.getBreakStart()==null?null: specialDay.getBreakStart().toLocalTime();
    }

    @Override
    public LocalTime getBreakEnd() {
        return specialDay.getBreakEnd()==null?null:specialDay.getBreakEnd().toLocalTime();
    }
}
