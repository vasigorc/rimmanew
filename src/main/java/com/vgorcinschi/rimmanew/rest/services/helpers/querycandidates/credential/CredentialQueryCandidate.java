package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential;

import java.util.Map;

/**
 *
 * @author vgorcinschi
 */
public abstract class CredentialQueryCandidate {
    protected Map<String, Object> params;
    protected Integer priority;
    protected CredentialQuerySignature querySignature;

    public CredentialQueryCandidate() {
    }

    public CredentialQueryCandidate(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Integer getPriority() {
        return priority;
    }

    public CredentialQuerySignature getQuerySignature() {
        return querySignature;
    }
}