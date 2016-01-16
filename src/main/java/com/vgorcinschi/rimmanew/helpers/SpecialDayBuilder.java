/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *this is a helper class that is intended to simplify
 * the construction of SpecialDay instances. That includes conversion between
 * client-friendly java.time API and SQL friendly java.util.sql.* classes
 * @author vgorcinschi
 */
public class SpecialDayBuilder {
    private final SpecialDay candidate;

    /*
        The only mandatory fields in the SpecialDay entity are 
        the id (DB provided) and the date. That is why a localDate
        instance is the only required argument to the constructor.
    */
    public SpecialDayBuilder(LocalDate ld) {
        this.candidate = new SpecialDay();
        this.candidate.setDate(Java8Toolkit.localToSqlDate(ld));
    }

    public SpecialDayBuilder setLd(LocalDate ld) {
        this.candidate.setDate(Java8Toolkit.localToSqlDate(ld));
        return this;
    }

    public SpecialDayBuilder setStartAt(LocalTime startAt) {
        this.candidate.setStartAt(Java8Toolkit.localToSqlTime(startAt));
        return this;
    }

    public SpecialDayBuilder setEndAt(LocalTime endAt) {
        this.candidate.setEndAt(Java8Toolkit.localToSqlTime(endAt));
        return this;
    }

    public SpecialDayBuilder setBreakStart(LocalTime breakStart) {
        this.candidate.setBreakStart(Java8Toolkit.localToSqlTime(breakStart));
        return this;
    }

    public SpecialDayBuilder setBreakEnd(LocalTime breakEnd) {
        this.candidate.setBreakEnd(Java8Toolkit.localToSqlTime(breakEnd));
        return this;
    }

    public SpecialDayBuilder setDuration(Duration duration) {
        this.candidate.setDuration(duration.toMinutes());
        return this;
    }

    public SpecialDayBuilder setIsBlocked(boolean isBlocked) {
        this.candidate.setIsBlocked(isBlocked);
        return this;
    }

    public SpecialDayBuilder setMessage(String message) {
        this.candidate.setMessage(message);
        return this;
    }
    
    public SpecialDay build(){
        return candidate;
    }
}
