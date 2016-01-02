/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.model.ScheduleDay;
import java.util.Optional;
import javax.ejb.Stateless;

/**
 *
 * @author vgorcinschi
 */
@Stateless
@Production
public class AvailabilitiesFacadeImpl implements AvailabilitiesFacade{
    private AppointmentRepository appointmentRepo;
    private SpecialDayRepository specialDayRepo;
    private NormalSchedule normalSchedule;

    @Override
    public Optional<ScheduleDay> searchAvailabilities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
