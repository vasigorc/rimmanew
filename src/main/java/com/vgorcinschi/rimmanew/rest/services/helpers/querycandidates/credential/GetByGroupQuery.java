package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential;

import java.util.Map;

/**
 *
 * @author vgorcinschi
 */
public class GetByGroupQuery extends CredentialQueryCandidate{

    public GetByGroupQuery(Map<String, Object> params) {
        super(params);
        this.priority = 2;
        this.querySignature = CredentialQuerySignature.getByGroup;
    }
}