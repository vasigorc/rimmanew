package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates;

import java.util.List;

/**
 *
 * @author vgorcinschi
 */
public interface QueryCommandControl<QueryCandidate, Entity, Repo> {
    public List<Entity> executeQuery(QueryCandidate cand, Repo repo);
}
