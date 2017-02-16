/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates;

import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import java.util.List;

/**
 *
 * @author vgorcinschi
 * the sole purpose of this interface is to
 * return a list of appointments
 */
@FunctionalInterface
public interface QueryCommand {
    List<Appointment> execute (AppointmentsQueryCandidate cand, AppointmentRepository repo);
}
