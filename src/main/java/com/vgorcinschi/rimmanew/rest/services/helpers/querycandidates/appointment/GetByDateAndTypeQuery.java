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
public class GetByDateAndTypeQuery extends AppointmentsQueryCandidate{

    public GetByDateAndTypeQuery(Map<String, Object> params) {
        super(params);
        this.priority=4;
        this.signature=AppointmentQuerySignature.getByDateAndType;
    }
    
}
