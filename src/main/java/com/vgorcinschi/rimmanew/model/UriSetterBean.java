/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CompanyProperties;
import java.io.Serializable;
import static java.lang.String.valueOf;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "companyPropertiesBean")
@SessionScoped
public class UriSetterBean implements Serializable {

    private String hostName, schemeName;
    private int port;
    private boolean urisUpdated, updateTried;
    
    @Inject
    @Production
    private CompanyProperties companyProperties;

    public UriSetterBean() {
        this.urisUpdated = false;
        this.updateTried = false;
    }
    
    public void setCompanyProperties(CompanyProperties companyProperties) {
        this.companyProperties = companyProperties;
    }
    
    public String getHostName() {
        return companyProperties.getHostName();
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getSchemeName() {
        return companyProperties.getSchemeName();
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public int getPort() {
        return companyProperties.getPort();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void updateUriProperties() {
        if (hostName == null || hostName.equals("") || schemeName == null || schemeName
                .equals("")) {
            updateTried = true;
            urisUpdated = false;
        } else {
            companyProperties.setHostName(hostName);
            companyProperties.setSchemeName(schemeName);
            if (valueOf(port) == null || port != 0) {
                companyProperties.setPort(port);
            } else //i.e. port is not part of the uri
            {
                companyProperties.setPort(-1);
            }
            updateTried = true;
            urisUpdated = true;
        }
    }

    public boolean isUrisUpdated() {
        return urisUpdated;
    }

    public void setUrisUpdated(boolean urisUpdated) {
        this.urisUpdated = urisUpdated;
    }

    public boolean isUpdateTried() {
        return updateTried;
    }

    public void setUpdateTried(boolean updateTried) {
        this.updateTried = updateTried;
    }
}
