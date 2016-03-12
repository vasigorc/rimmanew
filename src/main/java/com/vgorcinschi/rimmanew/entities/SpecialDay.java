/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vgorcinschi.rimmanew.rest.services.helpers.CustomTimeSerializer;
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
@JsonRootName(value="day with special schedule")
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonSerialize(using = CustomTimeSerializer.class)
    public Time getStartAt() {
        return startAt;
    }

    public void setStartAt(Time startAt) {
        this.startAt = startAt;
    }

    @Column(name = "end_at")
    @JsonSerialize(using = CustomTimeSerializer.class)
    public Time getEndAt() {
        return endAt;
    }

    public void setEndAt(Time endAt) {
        this.endAt = endAt;
    }

    @Column(name = "break_start")
    @JsonSerialize(using = CustomTimeSerializer.class)
    public Time getBreakStart() {
        return breakStart;
    }

    public void setBreakStart(Time breakStart) {
        this.breakStart = breakStart;
    }

    @Column(name = "break_end")
    @JsonSerialize(using = CustomTimeSerializer.class)
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
