package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.vgorcinschi.rimmanew.entities.Credential;
import java.util.List;

/**
 *
 * @author vgorcinschi
 */
public class JaxbCredentialListWrapperBuilder extends 
        GenericBaseJaxbListWrapperBuilder<Credential>{
    
    public JaxbCredentialListWrapperBuilder(int requestSize, int listSize, int requestOffset, List<Credential> current) {
        super(requestSize, listSize, requestOffset, current);
        this.pathToAppend = "credential";
        this.response = new JaxbCredentialListWrapper((List<Credential>) current);
    }
}