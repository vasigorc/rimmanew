package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.vgorcinschi.rimmanew.entities.Groups;
import java.util.List;

/**
 *
 * @author vgorcinschi
 */
public class JaxbGroupsListWrapperBuilder extends 
        GenericBaseJaxbListWrapperBuilder<Groups>{
    
    public JaxbGroupsListWrapperBuilder(int requestSize, int listSize, int requestOffset, List<Groups> current) {
        super(requestSize, listSize, requestOffset, current);
        this.pathToAppend = "groups";
        this.response = new JaxbGroupsListWrapper((List<Groups>) current);
    }    
}