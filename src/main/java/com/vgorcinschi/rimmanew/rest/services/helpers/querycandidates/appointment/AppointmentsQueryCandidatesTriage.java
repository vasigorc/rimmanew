/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.appointment;

import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QueryCandidateTriage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.PriorityQueue;

/**
 *
 * @author vgorcinschi Since we have a handful of available standardized select
 * queries configured through AppointmentRepository - we want to make sure to
 * select the most precise applicable based on user provided query params. This
 * will help us avoiding running the DB intensive getAll query. This class is a
 * variation on Specification Design Pattern. It creates all possible
 * queryCandidates and later picks the winner using a PriorityQueue
 */
public class AppointmentsQueryCandidatesTriage implements QueryCandidateTriage {

    private final PriorityQueue<AppointmentsQueryCandidate> pq;
    private final Map<String, Object> allProps;

    public AppointmentsQueryCandidatesTriage(String appDate, String appTime,
            String appType, String clientName) {
        Comparator<AppointmentsQueryCandidate> byPriority
                = (c1, c2) -> (Integer) (c2.getPriority() -c1.getPriority());
        pq = new PriorityQueue<>(byPriority);
        allProps = new HashMap();
        allProps.put("date", ofNullable(appDate).orElse(""));
        allProps.put("time", ofNullable(appTime).orElse(""));
        allProps.put("type", ofNullable(appType).orElse(""));
        allProps.put("name", ofNullable(clientName).orElse(""));
    }

    @Override
    public Optional<AppointmentsQueryCandidate> triage() {
        getByDate();
        getByDateAndTime();
        getByDateAndType();
        getByName();
        getByType();
        //by this point the PriorityQueue is only populated with non-null values
        return Optional.ofNullable(pq.peek());
    }

    @Override
    public Map<String, Object> allProps() {
        return allProps;
    }

    public void getByName() {
        if (!allProps.get("name").equals("")) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", allProps.get("name"));
            pq.offer(new GetByNameQuery(map));
        }
    }

    public void getByDate() {
        if (!allProps.get("date").equals("")) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", allProps.get("date"));
            pq.offer(new GetByDateQuery(map));
        }
    }

    public void getByType() {
        if (!allProps.get("type").equals("")) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", allProps.get("type"));
            pq.offer(new GetByTypeQuery(map));
        }
    }

    public void getByDateAndTime() {
        if (!allProps.get("date").equals("")
                && !allProps.get("time").equals("")) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", allProps.get("date"));
            map.put("time", allProps.get("time"));
            pq.offer(new GetByDateAndTimeQuery(map));
        }
    }

    public void getByDateAndType() {
        if (!allProps.get("date").equals("")
                && !allProps.get("type").equals("")) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", allProps.get("date"));
            map.put("type", allProps.get("type"));
            pq.offer(new GetByDateAndTypeQuery(map));
        }
    }
}
