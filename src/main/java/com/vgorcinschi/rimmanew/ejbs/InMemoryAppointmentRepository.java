/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.ejb.Singleton;
import javax.enterprise.inject.Default;

/**
 *
 * @author vgorcinschi
 */
@Singleton
@Default
public class InMemoryAppointmentRepository implements AppointmentRepository{

    private final Map<Long, Appointment> database = new Hashtable<>();
    private volatile long appointmentIdSequence = 1L;

    public InMemoryAppointmentRepository() {
    }
    
    @Override
    public void add(Appointment appointment) {
        appointment.setId(getNextAppointmentId());
        this.database.put(appointment.getId(), appointment);
    }

    @Override
    public void update(Appointment appointment) {
        this.database.put(appointment.getId(), appointment);
    }

    @Override
    public Appointment get(long id) {
        return this.database.get(id);
    }

    @Override
    public List<Appointment> getAll() {
        return new ArrayList<>(this.database.values());
    }

    @Override
    public List<Appointment> getByName(String name) {
        return database.values().stream().filter(a->a.getClientName().equalsIgnoreCase(name)).collect(toList());
    }

    @Override
    public List<Appointment> getByDate(Date date) {
        return database.values().stream().filter(a->a.getDate().equals(date)).sorted(comparing(Appointment::getDate)).collect(toList());
    }

    @Override
    public List<Appointment> getByType(String type) {
        return database.values().stream().filter(a->a.getType().equalsIgnoreCase(type)).collect(toList());
    }

    @Override
    public Appointment getByDateAndTime(Date date, Time time) {
        return database.values().stream().filter(a->a.getDate().equals(date)&&a.getTime().equals(time)).findAny().get();
    }

    @Override
    public List<Appointment> getByDateAndType(Date date, String type) {
        return database.values().stream().filter(a->a.getDate().equals(date)&&
                a.getType().equalsIgnoreCase(type)).sorted(comparing(Appointment::getDate)).collect(toList());
    }

    @Override
    public void deleteOne(Appointment appointment) {        
        if (database.values().stream().anyMatch(a->a.getId()==appointment.getId())) 
            database.remove(appointment.getId());        
    }

    @Override
    public void deleteAllBefore(Date date) {
        database.values().stream().filter(a->a.getDate().compareTo(date)<0).forEach(a->database.remove(a.getId()));
    }
    
    private synchronized long getNextAppointmentId(){
        return this.appointmentIdSequence++;
    }
}
