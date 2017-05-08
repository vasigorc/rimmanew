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
public class GetByDateQuery extends AppointmentsQueryCandidate{

    public GetByDateQuery(Map<String, Object> params) {
        super(params);
        this.priority=2;
        this.signature=AppointmentQuerySignature.getByDate;
    }
}
