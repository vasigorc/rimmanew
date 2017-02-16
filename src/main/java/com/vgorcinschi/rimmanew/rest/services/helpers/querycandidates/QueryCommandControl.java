/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates;

import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import static java.time.LocalDate.parse;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vgorcinschi
 */
public class QueryCommandControl {

    private final Map<QuerySignature, QueryCommand> queryCommands;

    public QueryCommandControl() {
        this.queryCommands = new HashMap<>();
        /*
         These should be called later like 
         queryCommands.get(QuerySignature.getByName).execute(concreteCandidate, realRepository);
         */
        queryCommands.put(QuerySignature.getByName,
                (AppointmentsQueryCandidate cand, AppointmentRepository repo) -> {
                    List<Appointment> l = repo.getByName((String) cand.getParams().get("name"));
                    return l;
                });
        queryCommands.put(QuerySignature.getByType,
                (AppointmentsQueryCandidate cand, AppointmentRepository repo) -> {
                    List<Appointment> l = repo.getByType((String) cand.getParams().get("type"));
                    return l;
                });
        queryCommands.put(QuerySignature.getByDate,
                (AppointmentsQueryCandidate cand, AppointmentRepository repo) -> {
                    List<Appointment> l = repo.getByDate(Java8Toolkit.localToSqlDate(
                                    parse((CharSequence) cand.getParams().get("date"))));
                    return l;
                });
        queryCommands.put(QuerySignature.getByDateAndType,
                (AppointmentsQueryCandidate cand, AppointmentRepository repo) -> {
                    List<Appointment> l = repo.getByDateAndType(Java8Toolkit.localToSqlDate(
                                    parse((CharSequence) cand.getParams().get("date"))),
                            (String) cand.getParams().get("type"));
                    return l;
                });
        queryCommands.put(QuerySignature.getByDateAndTime,
                (AppointmentsQueryCandidate cand, AppointmentRepository repo) -> {
                    List<Appointment> l = new LinkedList<>();
                    Appointment app = repo.getByDateAndTime(Java8Toolkit.localToSqlDate(
                                            parse((CharSequence) cand.getParams().get("date"))),
                                    Java8Toolkit.localToSqlTime(LocalTime.parse((CharSequence) cand.getParams().get("time"))));                    
                    l.add(app);
                    return l;
                });
    }

    //this is where we execute our query by dynamically picking the FunctionalInterface Impl
    public List<Appointment> executeQuery(AppointmentsQueryCandidate cand, AppointmentRepository repo) {
        return queryCommands.get(cand.getSignature()).execute(cand, repo);
    }
}
