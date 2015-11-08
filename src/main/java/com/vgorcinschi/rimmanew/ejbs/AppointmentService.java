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
public interface AppointmentService {
    void save(Appointment appointment);
    Appointment findById(long id);
    List<Appointment> findAll();
    List<Appointment> findByName(String name);
    List<Appointment> findByDate(Date date);
    List<Appointment> findByType(String type);
    Appointment findByDateAndTime(Date date, Time time);
    List<Appointment> findByDateAndType(Date date, String type);

    /**
     *
     * @param appointment
     * delete only this appointment
     */
    void deleteOne(Appointment appointment);
    void deleteAllBefore(Date date);
}
