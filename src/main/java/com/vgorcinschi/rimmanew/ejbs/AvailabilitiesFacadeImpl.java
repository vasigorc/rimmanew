/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.JpaRepository;
import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.entities.DivizableDay;
import com.vgorcinschi.rimmanew.helpers.ClosedDayHandler;
import com.vgorcinschi.rimmanew.model.ScheduleDay;
import com.vgorcinschi.rimmanew.helpers.ScheduleHandler;
import com.vgorcinschi.rimmanew.util.ExecutorFactoryProvider;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.concurrent.*;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author vgorcinschi
 */
@Stateless
@Production
public class AvailabilitiesFacadeImpl implements AvailabilitiesFacade {

    private AppointmentRepository appointmentRepo;
    private SpecialDayRepository specialDayRepo;
    private NormalSchedule normalSchedule;
    private Future<List<Appointment>> takenApps;
    private ScheduleHandler successor;
    private LocalDate requestedDay;
    private Executor executor;
    private ScheduleDay scheduleDay;
    private DivizableDay divizable;

    public AvailabilitiesFacadeImpl() {
    }

    @Inject
    public AvailabilitiesFacadeImpl(@JpaRepository AppointmentRepository appointmentRepo, 
            @JpaRepository SpecialDayRepository specialDayRepo, 
            @Production NormalSchedule normalSchedule) {
        this.appointmentRepo = appointmentRepo;
        this.specialDayRepo = specialDayRepo;
        this.normalSchedule = normalSchedule;
        this.executor = ExecutorFactoryProvider.getSingletonExecutorOf15();
        this.successor = new ClosedDayHandler(this);
    }

    @Override
    public Optional<ScheduleDay> searchAvailabilities(LocalDate l) {
        this.requestedDay = l;
        /*
         We want to have the list of already taken appointments by the time
         we find out the schedule type applicable for the day
         */
        takenApps = CompletableFuture.supplyAsync(() -> {
            return appointmentRepo.getByDate(localToSqlDate(l));
        }, executor);
        this.successor.handleRequest(requestedDay);
        this.successor = new ClosedDayHandler(this);
        return ofNullable(scheduleDay);
    }

    //will be consumed by Schedule Handlers upon a successfull match
    public Future<List<Appointment>> getTakenApps() {
        return takenApps;
    }

    public SpecialDayRepository getSpecialDayRepo() {
        return specialDayRepo;
    }

    public NormalSchedule getNormalSchedule() {
        return normalSchedule;
    }

    public ScheduleDay getScheduleDay() {
        return scheduleDay;
    }

    
    public void setScheduleDay(ScheduleDay scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public ScheduleHandler getSuccessor() {
        return successor;
    }

    public void setSuccessor(ScheduleHandler successor) {
        this.successor = successor;
    }

    public DivizableDay getDivizable() {
        return divizable;
    }

    public void setDivizable(DivizableDay divizable) {
        this.divizable = divizable;
    }
}
