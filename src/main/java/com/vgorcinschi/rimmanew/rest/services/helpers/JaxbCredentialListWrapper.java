package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.vgorcinschi.rimmanew.entities.Credential;
import java.util.List;

/**
 *
 * @author vgorcinschi
 */
@JsonRootName(value="credential")
public class JaxbCredentialListWrapper extends GenericBaseJaxbListWrapper<Credential> {

    public JaxbCredentialListWrapper(List<Credential> current) {
        super(current);
    }    
}