/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

import com.vgorcinschi.rimmanew.ejbs.AvailabilitiesFacadeImpl;
import com.vgorcinschi.rimmanew.model.NormalDaySchedule;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.getAvailabilitiesPerWorkingDay;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.noBreakInSchedule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author vgorcinschi
 */
public class NormalDayHandler implements ScheduleHandler {

    private final AvailabilitiesFacadeImpl af;

    public NormalDayHandler(AvailabilitiesFacadeImpl af) {
        this.af = af;
    }

    @Override
    public void handleRequest(LocalDate l) {
        List<LocalTime> normalDayAvails = new LinkedList<>();
        try {
            normalDayAvails = getAvailabilitiesPerWorkingDay
                    .apply(af.getDivizable(), af.getTakenApps().get(1500, TimeUnit.MILLISECONDS), noBreakInSchedule);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //TODO logging has to go here
        }
        af.setScheduleDay(new NormalDaySchedule(ofNullable(normalDayAvails)));
    }
}
