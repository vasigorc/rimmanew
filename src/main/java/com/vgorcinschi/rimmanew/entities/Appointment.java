/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateAdapter;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeAdapter;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author vgorcinschi
 */
@Entity
@NamedQuery(name = "findAllAppointments",
        query="SELECT a FROM Appointment a order by a.date DESC")
@Access(AccessType.PROPERTY)
@Table(name = "appointment")
@XmlRootElement(name="appointment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Appointment implements Serializable {

    @XmlElement
    private long id;
    @XmlJavaTypeAdapter(SqlDateAdapter.class)
    private Date date;
    @XmlJavaTypeAdapter(SqlTimeAdapter.class)
    private Time time;
    @XmlElement
    private String type;
    @XmlElement
    private String clientName;
    @XmlElement
    private String email;
    @XmlElement
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

   @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name="time")
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Column(name = "name")
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Column(name="email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="important")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Transient
    @JsonIgnore
    public LocalTime getLocalTimeRepr() {
        return time.toLocalTime();
    }
}
