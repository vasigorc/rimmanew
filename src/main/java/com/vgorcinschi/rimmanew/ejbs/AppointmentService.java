/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.model.AppointmentWrapper;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author vgorcinschi
 */
@Local
public interface AppointmentService {
    void save(Appointment appointment);
    AppointmentWrapper findById(long id);
    List<AppointmentWrapper> findAll();
    List<AppointmentWrapper> findByName(String name);
    List<AppointmentWrapper> findByDate(Date date);
    List<AppointmentWrapper> findByType(String type);
    AppointmentWrapper findByDateAndTime(Date date, Time time);
    List<AppointmentWrapper> findByDateAndType(Date date, String type);

    /**
     *
     * @param appointment
     * delete only this appointment
     */
    void deleteOne(Appointment appointment);
    void deleteAllBefore(Date date);
}
