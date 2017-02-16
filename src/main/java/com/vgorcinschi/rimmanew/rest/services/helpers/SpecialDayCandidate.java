/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 *
 * @author vgorcinschi
 */
@JsonRootName(value = "sd_candidate")
@JsonPropertyOrder({"id", "date", "startAt", "endAt", "breakStart", "breakEnd",
    "duration", "blocked", "message", "allowConflicts"})
public class SpecialDayCandidate {

    private int id;
    private String date, startAt, endAt, breakStart, breakEnd, duration, blocked,
            message, allowConflicts;

    public SpecialDayCandidate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty
    public String getDate() {
        return date;
    }

    @JsonProperty
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty
    public String getStartAt() {
        return startAt;
    }

    @JsonProperty
    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    @JsonProperty
    public String getEndAt() {
        return endAt;
    }

    @JsonProperty
    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    @JsonProperty
    public String getBreakStart() {
        return breakStart;
    }

    @JsonProperty
    public void setBreakStart(String breakStart) {
        this.breakStart = breakStart;
    }

    @JsonProperty
    public String getBreakEnd() {
        return breakEnd;
    }

    @JsonProperty
    public void setBreakEnd(String breakEnd) {
        this.breakEnd = breakEnd;
    }

    @JsonProperty
    public String getDuration() {
        return duration;
    }

    @JsonProperty
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @JsonProperty
    public String getBlocked() {
        return blocked;
    }

    @JsonProperty
    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    @JsonProperty
    public String getAllowConflicts() {
        return allowConflicts;
    }

    @JsonProperty
    public void setAllowConflicts(String allowConflicts) {
        this.allowConflicts = allowConflicts;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SpecialDayCandidate{" + "id=" + id + ", date=" + date + ", "
                + "startAt=" + startAt + ", endAt=" + endAt + ", breakStart="
                + breakStart + ", breakEnd=" + breakEnd + ", duration=" + duration
                + ", blocked=" + blocked + ", message=" + message
                + ", allowConflicts=" + allowConflicts + '}';
    }

}
