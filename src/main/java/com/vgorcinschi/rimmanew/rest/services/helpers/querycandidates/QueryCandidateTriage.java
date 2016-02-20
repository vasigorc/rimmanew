/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates;

import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 *
 * @author vgorcinschi
 */
public interface QueryCandidateTriage {

    /**
     *
     * @return PriorityQueue of QueryCandidates
     */
    Optional<?> triage();
    Map<String, Object> allProps();
}
