/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CompanyProperties;
import static com.vgorcinschi.rimmanew.util.InputValidators.stringNotNullNorEmpty;
import java.io.Serializable;
import static java.lang.String.valueOf;
import javax.annotation.PostConstruct;
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

    private String hostName, schemeName,suffix, javascriptLink;
    private final String restDirectory = "rest";
    private int port;
    private boolean urisUpdated, updateTried;

    @Inject
    @Production
    private CompanyProperties companyProperties;
    
    @PostConstruct
    public void init(){
        setHostName(companyProperties.getHostName());
        setSchemeName(companyProperties.getSchemeName());
        setPort(companyProperties.getPort());
        setSuffix(companyProperties.getSuffix());
    }

    public UriSetterBean() {
        this.urisUpdated = false;
        this.updateTried = false;
    }

    public void setCompanyProperties(CompanyProperties companyProperties) {
        this.companyProperties = companyProperties;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
        companyProperties.setHostName(hostName);
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
        companyProperties.setSchemeName(schemeName);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        companyProperties.setPort(port);
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        companyProperties.setSuffix(suffix);
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

    public String getJavascriptLink() {
        updateJavascriptLink();
        return javascriptLink;
    }

    public void setJavascriptLink(String javascriptLink) {
        this.javascriptLink = javascriptLink;
    }

    public void updateJavascriptLink() {
        StringBuilder result = new StringBuilder("");
        if (stringNotNullNorEmpty.apply(schemeName)) {
            result.append(schemeName).append("://");
        }
        result.append(hostName);
        if (valueOf(port) == null || port != 0) {
            result.append(":").append(port);
        }
        if (stringNotNullNorEmpty.apply(suffix)) {
            result.append("/").append(suffix);
        }
        result.append("/").append(restDirectory);
        setJavascriptLink(result.toString());
    }
}
