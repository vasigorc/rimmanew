package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential;

import java.util.Map;

/**
 *
 * @author vgorcinschi
 */
public class GetByUsernameQuery extends CredentialQueryCandidate{

    public GetByUsernameQuery(Map<String, Object> params) {
        super(params);
        //3 is highest priority, 1 is lowest
        this.priority = 3;
        this.querySignature = CredentialQuerySignature.getByUsername;
    }
}