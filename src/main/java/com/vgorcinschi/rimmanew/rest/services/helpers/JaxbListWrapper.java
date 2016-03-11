/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.vgorcinschi.rimmanew.entities.Appointment;
import java.net.URI;
import java.util.List;

/**
 *
 * @author vgorcinschi
 */
public interface JaxbListWrapper<R> {

    public List<R> getCurrent();
    public void setCurrent(List<R> current);
    public URI getFirst();
    public void setFirst(URI first);
    public URI getLast();
    public void setLast(URI last);
    public URI getNext();
    public void setNext(URI next);
    public URI getPrevious();
    public void setPrevious(URI previous);
    public URI getAll();
    public void setAll(URI all);
    public int getReturnedSize();
    public void setReturnedSize(int returnedSize);
}
