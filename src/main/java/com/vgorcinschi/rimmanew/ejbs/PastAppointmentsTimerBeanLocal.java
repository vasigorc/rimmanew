/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import javax.ejb.Local;

/**
 *
 * @author vgorcinschi
 */
@Local
public interface PastAppointmentsTimerBeanLocal {
    
    public void updatePastAppointments();
    public void deleteArchaicAppointments();
}
