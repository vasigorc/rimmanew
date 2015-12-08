/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import com.vgorcinschi.rimmanew.ejbs.AppointmentService;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.util.DateConverters;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import static com.vgorcinschi.rimmanew.util.Localizer.getLocalizedLabel;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import javax.enterprise.inject.Default;
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
    private Map<String, String> types;
    private LocalTime selectedTime;
    private String email, message, name, type;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    /**
     * Creates a new instance of AppointmentFormBean
     */
    @Inject
    private transient AppointmentService service;

    public AppointmentFormBean() {
        bookedAlready = new ArrayList<>();
        types = new LinkedHashMap<>(4, (float) 0.75);
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
        /*
         Here will go the logic about checking the status
         and the availabilities of the SceduleDay
         */
        setBookedAlready(service.findByDate(DateConverters.utilToSql(selectedDate)));
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

    /*
     This will be replaced altogether
     As the client doesn't need a list of appointments,
     but rather a list of availabilities. The given below
     is only a temporary hack
     */
    public List<LocalTime> getAvailabilities() {
        List<Integer> freeSpots = IntStream.rangeClosed(9, 16).filter(i -> i != 12).boxed().collect(toList());
        IntStream takenSpots = new LinkedList<>(getBookedAlready()).stream().mapToInt(wrapper
                -> wrapper.getTime().getHour());
        freeSpots.removeAll(takenSpots.boxed().collect(toList()));
        return freeSpots.stream().map(i -> LocalTime.of(i, 0)).collect(toList());
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

    public Pattern getVALID_EMAIL_ADDRESS_REGEX() {
        return VALID_EMAIL_ADDRESS_REGEX;
    }
}
