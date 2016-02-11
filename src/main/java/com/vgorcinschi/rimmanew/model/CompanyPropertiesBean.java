/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import com.vgorcinschi.rimmanew.util.PropertiesProvider;
import java.io.Serializable;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "companyPropertiesBean")
@SessionScoped
public class CompanyPropertiesBean implements Serializable {

    private String hostName, schemeName;
    private int port;
    private boolean urisUpdated;
    private boolean updateTried;

    @ManagedProperty("#{uriinfo}")
    private ResourceBundle uriinfo;

    @PostConstruct
    public void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        uriinfo = context.getApplication().evaluateExpressionGet(context, "#{uriinfo}", ResourceBundle.class);
        this.hostName = uriinfo.getString("host");
        this.port = parseInt(uriinfo.getString("port"));
        this.schemeName = uriinfo.getString("scheme");
    }

    /**
     * Creates a new instance of CompanyPropertiesBean
     */
    public CompanyPropertiesBean() {
        this.urisUpdated = false;
        this.updateTried = false;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public int getPort() {
        return port;
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
            PropertiesProvider.getUriProperties().setProperty("host", hostName);
            PropertiesProvider.getUriProperties().setProperty("scheme", schemeName);
            if (valueOf(port) == null || port != 0) {
                PropertiesProvider.getUriProperties().setProperty("port", valueOf(port));
            } else //i.e. port is not part of the uri
            {
                PropertiesProvider.getUriProperties().setProperty("port", "");
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
