/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author vgorcinschi
 */
@Stateless
public class DefaultAppointmentService implements AppointmentService {

    private AppointmentRepository repository;

    public DefaultAppointmentService() {
    }

    @Inject
    public DefaultAppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Appointment appointment) {
        if (findById(appointment.getId()) != null) 
            repository.update(appointment);
        else 
            repository.add(appointment);
        
    }

    @Override
    public Appointment findById(long id) {
        return repository.get(id);
    }

    @Override
    public List<Appointment> findByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public List<Appointment> findByDate(Date date) {
        return repository.getByDate(date);
    }

    @Override
    public List<Appointment> findByType(String type) {
        return repository.getByType(type);
    }

    @Override
    public Appointment findByDateAndTime(Date date, Time time) {
        return repository.getByDateAndTime(date, time);
    }

    @Override
    public List<Appointment> findByDateAndType(Date date, String type) {
        return repository.getByDateAndType(date, type);
    }

    @Override
    public void deleteOne(Appointment appointment) {
        repository.deleteOne(appointment);
    }

    @Override
    public void deleteAllBefore(Date date) {
        repository.deleteAllBefore(date);
    }

    @Override
    public List<Appointment> findAll() {
        return repository.getAll();
    }

}
