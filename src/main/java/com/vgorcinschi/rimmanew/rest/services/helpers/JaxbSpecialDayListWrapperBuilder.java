/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.vgorcinschi.rimmanew.entities.SpecialDay;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.genericTypeIdentifier;
import java.util.List;

/**
 *
 * @author vgorcinschi
 */
public class JaxbSpecialDayListWrapperBuilder 
    extends GenericBaseJaxbListWrapperBuilder<SpecialDay>{

    public JaxbSpecialDayListWrapperBuilder(int requestSize, int listSize, 
            int requestOffset, List<SpecialDay> current) throws SecurityException, NoSuchFieldException {
        super(requestSize, listSize, requestOffset, current);
        this.pathToAppend = "special day";
        this.response = new JaxbSpecialDayListWrapper((List<SpecialDay>) current);        
    }    
}
