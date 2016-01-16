/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author vgorcinschi
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "special_day")
public class SpecialDay implements Serializable {

    private long id;
    private Date date;
    private Time startAt, endAt, breakStart,
            breakEnd;
    private long duration;
    private boolean isBlocked;
    private String message;

    public SpecialDay() {
    }

    public SpecialDay(Date date, Time startAt, Time endAt, Time breakStart, 
            Time breakEnd, int duration, boolean isBlocked, String message) {
        this.date = date;
        this.startAt = startAt;
        this.endAt = endAt;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.duration = duration;
        this.isBlocked = isBlocked;
        this.message=message;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "start_at")
    public Time getStartAt() {
        return startAt;
    }

    public void setStartAt(Time startAt) {
        this.startAt = startAt;
    }

    @Column(name = "end_at")
    public Time getEndAt() {
        return endAt;
    }

    public void setEndAt(Time endAt) {
        this.endAt = endAt;
    }

    @Column(name = "break_start")
    public Time getBreakStart() {
        return breakStart;
    }

    public void setBreakStart(Time breakStart) {
        this.breakStart = breakStart;
    }

    @Column(name = "break_end")
    public Time getBreakEnd() {
        return breakEnd;
    }

    public void setBreakEnd(Time breakEnd) {
        this.breakEnd = breakEnd;
    }

    @Column(name = "duration_per_appointment")
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Column(name="is_blocked")
    public boolean isBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
