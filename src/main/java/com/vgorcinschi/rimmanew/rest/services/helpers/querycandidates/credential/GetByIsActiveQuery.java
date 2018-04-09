package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential;

import java.util.Map;

/**
 *
 * @author vgorcinschi
 */
public class GetByIsActiveQuery extends CredentialQueryCandidate{

    public GetByIsActiveQuery(Map<String, Object> params) {
        super(params);
        //1 is the lowest priority
        this.priority = 1;
        this.querySignature = CredentialQuerySignature.getByIsActive;
    }    
}