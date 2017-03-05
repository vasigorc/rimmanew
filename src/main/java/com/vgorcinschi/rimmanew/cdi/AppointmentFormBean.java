/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi;

import com.vgorcinschi.rimmanew.annotations.JpaService;
import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.AppointmentService;
import com.vgorcinschi.rimmanew.ejbs.AvailabilitiesFacade;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.helpers.InternationalizableDateBuilder;
import com.vgorcinschi.rimmanew.helpers.InternationalizableDateImpl;
import com.vgorcinschi.rimmanew.util.DateConverters;
import static com.vgorcinschi.rimmanew.util.DateConverters.utilToSql;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.getNextSuitableDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.isAWeekEnd;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToUtilDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.nextNotWeekEnd;
import com.vgorcinschi.rimmanew.util.Localizer;
import static com.vgorcinschi.rimmanew.util.Localizer.getLocalizedLabel;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import static java.time.LocalDate.now;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private List<AppointmentWrapper> bookedAlready;
    private final Map<String, String> types;
    private LocalTime selectedTime;
    private String name;
    private String email, message, type;
    private ScheduleDay schedD;
    
    @Inject
    @JpaService
    private AppointmentService service;
    
    @Inject
    @Production
    private AvailabilitiesFacade facade;
   
    public AppointmentFormBean() {
        bookedAlready = new ArrayList<>();
        types = new LinkedHashMap<>(4, (float) 0.75);
        selectedDate = localToUtilDate(getNextSuitableDate(now(), isAWeekEnd, nextNotWeekEnd));
        schedD = new UndefinedDaySchedule();
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
         //Checking the status and the availabilities of the SceduleDay
        setSchedD(facade.searchAvailabilities(selectedDate.toInstant()
                .atZone(ZoneId.of("America/Montreal")).toLocalDate()).get());
    }

    public boolean isDatePickerActivated() {
        return datePickerActivated;
    }

    public void setDatePickerActivated(boolean datePickerActivated) {
        this.datePickerActivated = datePickerActivated;
    }

    public List<AppointmentWrapper> getBookedAlready() {
        return bookedAlready;
    }

    public void setBookedAlready(List<AppointmentWrapper> dayAppointments) {
        this.bookedAlready = dayAppointments;
    }   

    public Map<String, String> getTypes() {
        if (types.isEmpty()) {
            types.put(getLocalizedLabel("manicure"), "manicure");
            types.put(getLocalizedLabel("massage"), "massage");
            types.put(getLocalizedLabel("pedicure"), "pedicure");
            types.put(getLocalizedLabel("waxing"), "waxing");
        }
        return types;
    }

    public LocalTime getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(LocalTime selectedTime) {
        this.selectedTime = selectedTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String bookAction(){
        Appointment candidate = new Appointment();
        candidate.setClientName(name);
        candidate.setDate(DateConverters.utilToSql(selectedDate));
        candidate.setEmail(email);
       candidate.setMessage(message);
        candidate.setTime(Java8Toolkit.localToSqlTime(selectedTime));
        candidate.setType(type);
        /*
            below will go the logic to request verification
            from a stateless EJB for the Appointment. Might as well be
            a util method. Surround by if statement.
        */
        service.save(candidate);
        return "booked";
    }
    
    //a convenience method to get a workable date format for the view
    public InternationalizableDateImpl displayDate(){
        return new InternationalizableDateImpl(new InternationalizableDateBuilder
        (utilToSql(this.selectedDate).toLocalDate()).setDayOfWeekStyle(TextStyle.FULL)
        .setMonthsStyle(TextStyle.SHORT).setSessionLocale(Localizer.getCurrentViewRoot().getLocale()));
    }
    
    public String getLocalizedType(){
        return getLocalizedLabel(type);
    }

    public ScheduleDay getSchedD() {
        return schedD;
    }

    public void setSchedD(ScheduleDay schedD) {
        this.schedD = schedD;
    }
}
