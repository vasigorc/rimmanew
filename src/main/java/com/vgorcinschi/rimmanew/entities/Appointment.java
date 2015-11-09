/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

/**
 *
 * @author vgorcinschi
 */
public class Appointment{

    private long id;
    private Date date;
    private Time time;
    private String type;
    private String clientName;
    private String email;
    private String message;

    public Appointment() {
    }

    public Appointment(long id, Date date, Time time, String type, String clientName, String email, String message) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.clientName = clientName;
        this.email = email;
        this.message = message;
    }

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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }    
    
    public LocalTime getLocalTimeRepr() {        
        return time.toLocalTime();
    }
}
