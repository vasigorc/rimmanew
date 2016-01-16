/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

import com.vgorcinschi.rimmanew.ejbs.AvailabilitiesFacadeImpl;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.model.ClosedDaySchedule;
import java.time.LocalDate;
import java.util.Optional;
import static java.util.Optional.ofNullable;

/**
 *
 * @author vgorcinschi
 */
public class ClosedDayHandler implements ScheduleHandler {

    private final AvailabilitiesFacadeImpl af;

    public ClosedDayHandler(AvailabilitiesFacadeImpl af) {
        this.af = af;
    }

    @Override
    public void handleRequest(LocalDate l) {
        Optional<SpecialDay> specialDay = ofNullable(af.getSpecialDayRepo().getSpecialDay(l));
        //both condition must match, otherwise this day could be on a normal
        //schedule (very probable) and the above query will return null
        if (specialDay.isPresent() && specialDay.get().isBlocked()) {
            ClosedDaySchedule csd = new ClosedDaySchedule();
            csd.setMessage(specialDay.get().getMessage());
            af.setScheduleDay(csd);
        } else if (!specialDay.isPresent()) {
            //i.e. there's no Special Day and this day is a Normal Schedule
            //TODO set the successor to NormalDayHandler
            af.setDivizable(af.getNormalSchedule());
            af.setSuccessor(new NormalDayHandler(af));
            af.getSuccessor().handleRequest(l);
        } else {
            //we are passing the retrieved entity to the next cell in the 
            //chain of responsability so to next run the same query again
            //we do not need to check if the objec is present in the optional
            //wrapper - already accomplished above (l. 30) 
            af.setDivizable(new SpecialDayDivizableAdapter(specialDay.get()));
            //passing on the baton to the next Handler
            af.setSuccessor(new SpecialDayHandler(af));
            //calling the .handleRequest() method of the next handler
            af.getSuccessor().handleRequest(l);
        }
    }

}
