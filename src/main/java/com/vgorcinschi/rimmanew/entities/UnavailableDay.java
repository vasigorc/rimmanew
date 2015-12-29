/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author vgorcinschi
 */
@Entity
@NamedQuery(name = "findUnavailableDayByDate",
        query = "SELECT u FROM UnavailableDay u "
                + "WHERE u.date = :date")
@Access(AccessType.PROPERTY)
@Table(name = "unavailable_day")
public class UnavailableDay implements Serializable {
    
    private long id;
    private Date date;
    private String message;
    private boolean isInThePast;
    private String status;

    public UnavailableDay() {
    }

    public UnavailableDay(Date date, String message, boolean isInThePast, String status) {
        this.date = date;
        this.message = message;
        this.isInThePast = isInThePast;
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name="is_in_the_past")
    public boolean isIsInThePast() {
        return isInThePast;
    }

    public void setIsInThePast(boolean isInThePast) {
        this.isInThePast = isInThePast;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
