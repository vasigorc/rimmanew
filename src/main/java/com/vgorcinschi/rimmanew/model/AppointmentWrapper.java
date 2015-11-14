/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import com.vgorcinschi.rimmanew.entities.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.*;

/**
 *
 * @author vgorcinschi
 */
public class AppointmentWrapper {
    private final Optional<Appointment> safeAppointment;

    public AppointmentWrapper(Optional<Appointment> safeAppointment) {
        this.safeAppointment = safeAppointment;
    }
    
    public String getType(){
        return safeAppointment.map(Appointment::getType).orElse("notype");
    }
    
    /**
     * a little explanation as to why we are
     * @return ing an instance of now in the orElseGet method:
     * we will convent that the appointment is taking place now which will
     * force the administrator to reach out to the client and check what is going on
     */
    public LocalDate getDate(){
        return safeAppointment.map(a->a.getDate().toLocalDate()).orElseGet(LocalDate::now);
    }
    
    /*
        same logic as above
    */
    public LocalTime getTime(){
        return safeAppointment.map(a->a.getTime().toLocalTime()).orElseGet(LocalTime::now);
    }
    
    public String getClientName(){
        return safeAppointment.map(Appointment::getClientName).orElse("noname");
    }
    
    public String getClientEmail(){
        return safeAppointment.map(Appointment::getEmail).orElse("noemail");
    }
    
    public String getClientMessage(){
        return safeAppointment.map(Appointment::getMessage).orElse("");
    }
    
    /**
        If by an improbable concurrence of circumstances this method will
       @return a new Appointment instance it will then be persisted
       to the database
    */
    public Appointment getEntity(){
        return safeAppointment.orElseGet(Appointment::new );
    }
    
    public void setType(String type){
        getEntity().setType(type);
    }
    
    public void setTime(LocalTime newTime){
        getEntity().setTime(localToSqlTime(newTime));
    }
    
    public void setDate(LocalDate date){
        getEntity().setDate(localToSqlDate(date));
    }
    
    public void setClientName(String name){
        getEntity().setClientName(name);
    }
    
    public void setClientMessage(String message){
        getEntity().setMessage(message);
    }
    
    public void setClientEmail(String email){
        getEntity().setMessage(email);
    }
    
    public boolean isEmpty(){
        return !safeAppointment.isPresent();
    }
}
