/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi;

import static com.vgorcinschi.rimmanew.util.Localizer.getCurrentViewRoot;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "scheduleManagementBean")
@SessionScoped
public class ScheduleManagementBean implements Serializable {

    private String displayLanguage = getCurrentViewRoot().getLocale().toString();
    /**
     * Creates a new instance of ScheduleManagementBean
     */
    public ScheduleManagementBean() {
    }

    public String getDisplayLanguage() {
        return displayLanguage;
    }

    public void setDisplayLanguage(String displayLanguage) {
        this.displayLanguage = displayLanguage;
    }
    
}
