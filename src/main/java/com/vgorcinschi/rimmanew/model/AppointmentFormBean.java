/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import com.vgorcinschi.rimmanew.ejbs.AppointmentService;
import com.vgorcinschi.rimmanew.util.DateConverters;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "appointmentFormBean")
@SessionScoped
public class AppointmentFormBean implements Serializable {

    private Date selectedDate;
    private boolean datePickerActivated = false;
    private List<AppointmentWrapper> dayAppointments;
    /**
     * Creates a new instance of AppointmentFormBean
     */
    @EJB
    private transient AppointmentService service;

    public AppointmentFormBean() {        
        dayAppointments = new ArrayList<>();
    }

    //constructor just for mock testing
    public AppointmentFormBean(AppointmentService service) {
        this.service = service;
        dayAppointments = new ArrayList<>();
    }

    public AppointmentService getService() {
        return service;
    }

    public void setService(AppointmentService service) {
        this.service = service;
    }
    
    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
        setDatePickerActivated(true);
        setDayAppointments(service.findByDate(DateConverters.utilToSql(selectedDate)));
    }

    public boolean isDatePickerActivated() {
        return datePickerActivated;
    }

    public void setDatePickerActivated(boolean datePickerActivated) {
        this.datePickerActivated = datePickerActivated;
    }

    public List<AppointmentWrapper> getDayAppointments() {
        return dayAppointments;
    }

    public void setDayAppointments(List<AppointmentWrapper> dayAppointments) {
        this.dayAppointments = dayAppointments;
    }
}
