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
import javax.ejb.Local;

/**
 *
 * @author vgorcinschi
 */
@Local
public interface AppointmentRepository {
    void add(Appointment appointment);
    void update(Appointment appointment);
    Appointment get(long id);
    List<Appointment> getAll();
    List<Appointment> getByName(String name);
    List<Appointment> getByDate(Date date);
    List<Appointment> getByType(String type);
    Appointment getByDateAndTime(Date date, Time time);
    List<Appointment> getByDateAndType(Date date, String type);

    /**
     *
     * @param appointment
     * delete only this appointment
     */
    void deleteOne(Appointment appointment);
    void deleteAllBefore(Date date);
}
