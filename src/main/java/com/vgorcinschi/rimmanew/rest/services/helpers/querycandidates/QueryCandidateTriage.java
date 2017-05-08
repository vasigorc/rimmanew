package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates;

import java.util.Map;
import java.util.Optional;

/**
 *
 * @author vgorcinschi
 */
public interface QueryCandidateTriage {

    Optional<?> triage();
    Map<String, Object> allProps();
}