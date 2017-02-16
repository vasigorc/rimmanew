/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

/**
 *
 * @author vgorcinschi
 */
public interface JaxbListWrapperBuilder {
    
    public GenericBaseJaxbListWrapper compose();
    public void setFirstURI();
    public void setLastURI();
    public void setNextURI();
    public void setPreviousURI();
}
