/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.InMemoryRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.model.AppointmentWrapper;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 *
 * @author vgorcinschi
 */
@Stateless
@Default
public class DefaultAppointmentService implements AppointmentService {

    private AppointmentRepository repository;

    public DefaultAppointmentService() {
    }

    @Inject
    public DefaultAppointmentService(@InMemoryRepository AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Appointment appointment) {
        if (findById(appointment.getId()) != null) 
            repository.update(appointment);
        else 
            repository.add(appointment);
        
    }

    /*
        if the appointment doesn't exist - we are returning an empty Optional
    */
    @Override
    public AppointmentWrapper findById(long id) {
        return new AppointmentWrapper(ofNullable(repository.get(id)));
    }

    @Override
    public List<AppointmentWrapper> findByName(String name) {
        return repository.getByName(name).stream()
                .map(appointment->{return new AppointmentWrapper(ofNullable(appointment));})
                .collect(toList());
    }

    @Override
    public List<AppointmentWrapper> findByDate(Date date) {
        return repository.getByDate(date).stream()
                .map(appointment->{return new AppointmentWrapper(ofNullable(appointment));})
                .collect(toList());
    }

    @Override
    public List<AppointmentWrapper> findByType(String type) {
        return repository.getByType(type).stream()
                .map(appointment->{return new AppointmentWrapper(ofNullable(appointment));})
                .collect(toList());
    }

    @Override
    public AppointmentWrapper findByDateAndTime(Date date, Time time) {
        return new AppointmentWrapper(ofNullable(repository.getByDateAndTime(date, time)));
    }

    @Override
    public List<AppointmentWrapper> findByDateAndType(Date date, String type) {
        return repository.getByDateAndType(date, type).stream()
                .map(appointment->{return new AppointmentWrapper(ofNullable(appointment));})
                .collect(toList());
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
    public List<AppointmentWrapper> findAll() {
        return repository.getAll().stream()
                .map(appointment->{return new AppointmentWrapper(ofNullable(appointment));})
                .collect(toList());
    }

}
