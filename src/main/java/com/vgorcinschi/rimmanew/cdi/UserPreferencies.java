/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi;

import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author vgorcinschi
 */
@Named
@SessionScoped
public class UserPreferencies implements Serializable {

    private Locale currentLocale;

    public UserPreferencies() {
        //locale is initiated to French but may be overriden in the
        //post-constructed method
        currentLocale = new Locale("fr");
    }

    @PostConstruct
    public void initialize() {
        /**
         * if a user specific locale is provided in the browser
         * we should be setting it here
         */
        if(FacesContext.getCurrentInstance() != null){
            Optional<ExternalContext> optContext = ofNullable(FacesContext.getCurrentInstance()
                    .getExternalContext());
            if (optContext.isPresent()) {
                Optional<Locale> userLocale = ofNullable(optContext.get().getRequestLocale());
                if (userLocale.isPresent()) {
                    currentLocale = userLocale.get();
                }
            }
        }
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }
    
}
