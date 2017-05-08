/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.appointment;

import java.util.Map;

/**
 *
 * @author vgorcinschi
 */
public abstract class AppointmentsQueryCandidate {
    protected Map<String, Object> params;
    protected Integer priority;
    protected AppointmentQuerySignature signature;

    protected AppointmentsQueryCandidate() {
    }

    protected AppointmentsQueryCandidate(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public int getPriority() {
        return priority;
    }

    public AppointmentQuerySignature getSignature() {
        return signature;
    }   
}