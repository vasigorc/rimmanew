package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates;

import java.util.List;

/**
 *
 * @author vgorcinschi
 * the sole purpose of this interface is to
 * return a list of appointments
 */
@FunctionalInterface
public interface QueryCommand <QueryCandidate, Entity, Repo>{
    List<Entity> execute (QueryCandidate cand, Repo repo);
}
