/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import com.vgorcinschi.rimmanew.util.PropertiesProvider;
import static java.lang.String.valueOf;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "companyPropertiesBean")
@ApplicationScoped
public class CompanyPropertiesBean {

    private String hostName, schemeName;
    private int port;
    /**
     * Creates a new instance of CompanyPropertiesBean
     */
    public CompanyPropertiesBean() {
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
    
    public boolean updateUriProperties(){
        if (hostName==null||hostName.equals("")||schemeName==null||schemeName
                .equals("")) {
            return false;
        }
        PropertiesProvider.getUriProperties().setProperty("host", hostName);
        PropertiesProvider.getUriProperties().setProperty("scheme", schemeName);
        if (valueOf(port)==null||port!=0)
            PropertiesProvider.getUriProperties().setProperty("port", valueOf(port));
        else
            //i.e. port is not part of the uri
            PropertiesProvider.getUriProperties().setProperty("port", "");
        return true;
    }
}
