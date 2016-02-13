/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vgorcinschi.rimmanew.entities.Appointment;
import java.net.URI;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vgorcinschi
 */
@XmlRootElement(name="appointments")
@JsonInclude(Include.NON_NULL)
public class JaxbAppointmentListWrapper {

    private List<Appointment> current;
    private URI first, last, next, previous, all;
    private int returnedSize;

    public JaxbAppointmentListWrapper(List<Appointment> current) {
        this.current = current;
    }

    @JsonGetter
    public List<Appointment> getCurrent() {
        return current;
    }

    public void setCurrent(List<Appointment> current) {
        this.current = current;
    }

    @JsonGetter    
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getFirst() {
        return first;
    }

    public void setFirst(URI first) {
        this.first = first;
    }

    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getLast() {
        return last;
    }

    public void setLast(URI last) {
        this.last = last;
    }

    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getNext() {
        return next;
    }

    public void setNext(URI next) {
        this.next = next;
    }

    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getPrevious() {
        return previous;
    }

    public void setPrevious(URI previous) {
        this.previous = previous;
    }

    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getAll() {
        return all;
    }

    public void setAll(URI all) {
        this.all = all;
    }

    @JsonProperty("size")
    public int getReturnedSize() {
        return returnedSize;
    }

    public void setReturnedSize(int returnedSize) {
        this.returnedSize = returnedSize;
    }
}
