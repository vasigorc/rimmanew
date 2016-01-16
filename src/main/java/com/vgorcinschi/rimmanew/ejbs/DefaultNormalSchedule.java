/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.Production;
import java.time.Duration;
import java.time.LocalTime;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author vgorcinschi
 */
@Singleton
@Startup
@Production
public class DefaultNormalSchedule implements NormalSchedule{

    private LocalTime startAt, endAt, breakStart, breakEnd;
    private Duration duration;

    public DefaultNormalSchedule() {
        this.startAt = LocalTime.of(9, 0);
        this.endAt = LocalTime.of(17, 0);
        this.duration = Duration.ofMinutes(60);
        this.breakStart = LocalTime.of(12, 0);
        this.breakEnd = LocalTime.of(13, 0);
    }

    @Override
    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public void setStartAt(LocalTime startAt) {
        this.startAt = startAt;
    }

    @Override
    public LocalTime getEndAt() {
        return endAt;
    }

    @Override
    public void setEndAt(LocalTime endAt) {
        this.endAt = endAt;
    }

    @Override
    public LocalTime getBreakStart() {
        return breakStart;
    }

    @Override
    public void setBreakStart(LocalTime breakStart) {
        this.breakStart = breakStart;
    }

    @Override
    public LocalTime getBreakEnd() {
        return breakEnd;
    }
    
    @Override
    public void setBreakEnd(LocalTime breakEnd) {
        this.breakEnd = breakEnd;
    }

    /**
     *
     * @return
     * the duration of an appointment
     */
    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    
    
}
