/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "appointmentFormBean")
@SessionScoped
public class AppointmentFormBean implements Serializable, Observed{
    private Date selectedDate;
    private ArrayList<VGObserver> observers;
    private boolean datePickerActivated = false;
    /**
     * Creates a new instance of AppointmentFormBean
     */
    public AppointmentFormBean() {
        observers = new ArrayList<>();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
       // notifyVGObservers();
        setDatePickerActivated(true);
    }

    @Override
    public void registerVGObserver(VGObserver o) {
        this.observers.add(o);
    }

    @Override
    public void removeVGObserver(VGObserver o) {
        int i = observers.indexOf(o);
        if(i>=0)
            observers.remove(o);
    }

    @Override
    public void notifyVGObservers() {
        observers.stream().map((observer1) -> (VGObserver) observer1).forEach((observer) -> {
            observer.update(selectedDate);
        });
    }

    public boolean isDatePickerActivated() {
        return datePickerActivated;
    }

    public void setDatePickerActivated(boolean datePickerActivated) {
        this.datePickerActivated = datePickerActivated;
    }
}
