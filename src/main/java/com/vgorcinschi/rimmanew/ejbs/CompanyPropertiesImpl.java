/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.util.PropertiesProvider;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vgorcinschi
 */
@Singleton
@Startup
@Production
public class CompanyPropertiesImpl implements CompanyProperties {

    private int daysBeforeForceDeletingTheAppointmentRecord;
    private int daysBeforeMarkingAsPast;
    private final Logger log = LogManager.getLogger();
    /*
     These properties file should be just a pointer to the real Properties
     singleton object that is shared throughout the application
     */
    private final Properties uriProperties;

    public CompanyPropertiesImpl() {
        uriProperties = PropertiesProvider.getUriProperties();
        daysBeforeMarkingAsPast = 7;
        daysBeforeForceDeletingTheAppointmentRecord = 730;//two years by default
    }

    @PostConstruct
    public void init() {
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("uriinfo.properties");
        if (inputStream == null) {
            log.fatal("uriinfo.properties file could not be found. Rest services "
                    + "may stop functioning.");
        } else {
            try {
                uriProperties.load(inputStream);
                log.info("The information from uriinfo.properties has been successfully "
                        + "uploaded: " + inputStream);
            } catch (IOException ex) {
                log.fatal("uriinfo.properties file could not be loaded. Rest services "
                        + "may stop functioning.");
            }
        }
    }

    @Override
    public String getHostName() {
        return this.uriProperties.getProperty("host");
    }

    @Override
    public void setHostName(String hostName) {
        this.uriProperties.setProperty("host", hostName);
    }

    @Override
    public String getSchemeName() {
        return this.uriProperties.getProperty("scheme");
    }

    @Override
    public void setSchemeName(String schemeName) {
        this.uriProperties.setProperty("scheme", schemeName);
    }

    @Override
    public int getPort() {
        try {
            return parseInt(this.uriProperties.getProperty("port"));
        } catch (NumberFormatException e) {
            log.error("Stored port number is not a number. NumberFormatException"
                    + ": " + e.getMessage());
            return -1;
        }
    }

    @Override
    public void setPort(int port) {
        if (port == -1) {
            this.uriProperties.setProperty("port", "");
            log.warn("Port number skipped when updating the uri properties.");
        } else {
            this.uriProperties.setProperty("port", valueOf(port));
        }
    }

    @Override
    public void setDaysBeforeMarkingAsPast(int days, HttpServletRequest request) {
        if (days <= 0) {
            if (request.getRemoteAddr() != null) {
                log.error("Attempt to set days to before marking appointmes as past "
                        + "to 0 or a negative number. From the IP address: " + request.getRemoteAddr());
            }
            log.error("Blocked attempt to set days to before marking appointmes as past "
                    + "to 0 or a negative number.");
        } else {
            this.daysBeforeForceDeletingTheAppointmentRecord = days;
        }
    }

    @Override
    public void setDaysBeforeForceDeletingTheAppointmentRecord(int days, HttpServletRequest request) {
        if (days <= 0) {
            if (request.getRemoteAddr() != null) {
                log.error("Attempt to set days to force delete appointment records "
                        + "to 0 or a negative number. From the IP address: " + request.getRemoteAddr());
            }
            log.error("Blocked attempt to set days to force delete appointment records "
                    + "to 0 or a negative number.");
        } else {
            this.daysBeforeForceDeletingTheAppointmentRecord = days;
        }
    }

    @Override
    public int getDaysBeforeForceDeletingTheAppointmentRecord() {
        return daysBeforeForceDeletingTheAppointmentRecord;
    }

    @Override
    public int getDaysBeforeMarkingAsPast() {
        return daysBeforeMarkingAsPast;
    }

}
