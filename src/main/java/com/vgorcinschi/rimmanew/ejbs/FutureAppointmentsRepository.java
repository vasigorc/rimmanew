/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author vgorcinschi
 */
public interface FutureAppointmentsRepository extends AppointmentRepository {

    List<Appointment> getMarkedOngoingAndBeforeDate(Date date);

    /*
     UPDATE User u
     SET u.status = 'G'
     WHERE u.numTrades >=?1
     */
    int batchSetIsPassedStatus(Date before);
}
