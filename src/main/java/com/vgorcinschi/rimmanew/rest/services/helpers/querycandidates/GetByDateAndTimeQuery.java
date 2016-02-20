/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates;

import java.util.Map;

/**
 *
 * @author vgorcinschi
 */
public class GetByDateAndTimeQuery extends AppointmentsQueryCandidate {

    public GetByDateAndTimeQuery(Map<String, Object> params) {
        super(params);
        this.priority=5;
        this.signature=QuerySignature.getByDateAndTime;
    }
    
}
