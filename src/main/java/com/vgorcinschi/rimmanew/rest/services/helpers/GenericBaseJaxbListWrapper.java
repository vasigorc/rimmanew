/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.net.URI;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vgorcinschi
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public abstract class GenericBaseJaxbListWrapper<R> implements JaxbListWrapper<R> {

    protected List<R> current;
    protected URI first, last, next, previous, all;
    protected int returnedSize;

    public GenericBaseJaxbListWrapper(List<R> current) {
        this.current = current;
    }

    @Override
    public void setReturnedSize(int returnedSize) {
        this.returnedSize = returnedSize;
    }

    @Override
    @JsonProperty("currentReturnedSize")
    public int getReturnedSize() {
        return returnedSize;
    }

    @Override
    public void setAll(URI all) {
        this.all = all;
    }

    @Override
    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getAll() {
        return all;
    }

    @Override
    public void setPrevious(URI previous) {
        this.previous = previous;
    }

    @Override
    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getPrevious() {
        return previous;
    }

    @Override
    public void setNext(URI next) {
        this.next = next;
    }

    @Override
    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getNext() {
        return next;
    }

    @Override
    public void setLast(URI last) {
        this.last = last;
    }

    @Override
    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getLast() {
        return last;
    }

    @Override
    public void setFirst(URI first) {
        this.first = first;
    }

    @Override
    @JsonGetter
    @JsonSerialize(using = URIJsonSerializer.class)
    public URI getFirst() {
        return first;
    }

    @Override
    public void setCurrent(List<R> current) {
        this.current = current;
    }

    @Override
    @JsonGetter
    public List<R> getCurrent() {
        return current;
    }
}
