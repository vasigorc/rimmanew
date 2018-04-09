package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.vgorcinschi.rimmanew.entities.Groups;
import java.util.List;

/**
 *
 * @author vgorcinschi
 */
@JsonRootName(value="groups")
public class JaxbGroupsListWrapper extends GenericBaseJaxbListWrapper<Groups>{
    
    public JaxbGroupsListWrapper(List<Groups> current) {
        super(current);
    }
}