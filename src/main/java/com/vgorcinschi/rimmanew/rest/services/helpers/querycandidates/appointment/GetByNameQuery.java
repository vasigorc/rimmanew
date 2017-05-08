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
public class GetByNameQuery extends AppointmentsQueryCandidate {
    
    public GetByNameQuery(Map<String, Object> params) {
        super(params);
        this.priority=3;
        this.signature=AppointmentQuerySignature.getByName;
    }
}
