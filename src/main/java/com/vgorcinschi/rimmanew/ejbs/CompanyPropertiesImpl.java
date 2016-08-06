/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.util.PropertiesProvider;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import java.util.Properties;
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
    private final int daysBeforeMarkingAsPast;
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
    public String getSuffix() {
        return this.uriProperties.getProperty("suffix");
    }

    @Override
    public void setSuffix(String suffix) {
        this.uriProperties.setProperty("suffix", suffix);
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
        if (request == null || request.getRemoteAddr() == null) {
            log.warn("Attempt to set days to force delete appointments from "
                    + days + " days in the past and before came not from a "
                    + "http request.");
            this.daysBeforeForceDeletingTheAppointmentRecord = days;
            log.debug("The number of days to force delete appointment records"
                    + " from the system has been set to " + days + " days in the past.");
        } else {
            if (days <= 0) {
                log.error("Attempt to set days to force delete appointment records "
                        + "to 0 or a negative number. From the IP address: " + request.getRemoteAddr());
            } else {
                this.daysBeforeForceDeletingTheAppointmentRecord = days;
                log.debug("The number of days to force delete appointment records"
                        + " from the system has been set to " + days + " days in the past. "
                        + "The request was submitted from the IP address: "
                        + request.getRemoteAddr());
            }
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
