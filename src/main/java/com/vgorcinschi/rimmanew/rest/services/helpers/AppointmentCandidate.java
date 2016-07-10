/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import static com.vgorcinschi.rimmanew.util.InputValidators.stringNotNullNorEmpty;
import java.sql.Date;

/**
 *
 * @author vgorcinschi
 */
@JsonRootName(value = "appointment_candidate")
@JsonPropertyOrder({"id", "date", "time", "type", "clientName", "email", "message"})
public class AppointmentCandidate {

    public int id;
    public Date appDate;
    public String time, appType, clientName, clientEmail, clientMsg, past, noShow;

    public AppointmentCandidate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("date")
    @JsonDeserialize(using = SqlDateDeserializer.class)
    public Date getAppDate() {
        return appDate;
    }

    @JsonProperty("date")
    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    @JsonProperty
    public String getTime() {
        return time;
    }

    @JsonProperty
    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("type")
    public String getAppType() {
        return appType;
    }

    @JsonProperty("type")
    public void setAppType(String appType) {
        this.appType = appType;
    }

    @JsonProperty
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @JsonProperty("email")
    public String getClientEmail() {
        return clientEmail;
    }

    @JsonProperty("email")
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    @JsonProperty("message")
    public String getClientMsg() {
        return clientMsg;
    }

    @JsonProperty("message")
    public void setClientMsg(String clientMsg) {
        this.clientMsg = clientMsg;
    }

    @JsonProperty
    public String isPast() {
        return past;
    }

    public void setPast(String past) {
        this.past = past;
    }

    @JsonProperty
    public String isNoShow() {
        return noShow;
    }

    public void setNoShow(String noShow) {
        this.noShow = noShow;
    }

    @Override
    public String toString() {
        return (this.appDate == null) ? this.clientName + ": " + ", at " + this.time
                + "\nComing for a " + this.appType + ". Email: "
                + this.clientEmail + ((stringNotNullNorEmpty.apply(this.clientMsg)
                        ? "\nThis is"
                        + " their message: " + this.clientMsg : "")) : this.clientName
                + ": " + this.appDate.toLocalDate() + ", at " + this.time
                + "\nComing for a " + this.appType + ". Email: " + this.clientEmail
                + ((stringNotNullNorEmpty.apply(this.clientMsg) ? "\nThis is"
                        + " their message: " + this.clientMsg : ""));
    }

}
