/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "userManager")
@SessionScoped
public class CredentialManagementBean implements Serializable {

    //inject two @Production repositories
    /**
     * Creates a new instance of CredentialManagementBean
     */
    public CredentialManagementBean() {
    }
    
}
