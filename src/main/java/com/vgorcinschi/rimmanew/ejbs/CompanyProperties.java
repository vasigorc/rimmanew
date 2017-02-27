/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.ejb.AccessTimeout;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author vgorcinschi
 */
@Local
@Lock(LockType.WRITE)
public interface CompanyProperties {

    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    String getHostName();
    void setHostName(String hostName);
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    String getSchemeName();
    void setSchemeName(String schemeName);
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    int getPort();
    void setPort(int port);
    void setDaysBeforeMarkingAsPast(int days, HttpServletRequest request);
    void setDaysBeforeForceDeletingTheAppointmentRecord(int days, HttpServletRequest request);
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    int getDaysBeforeMarkingAsPast();
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    int getDaysBeforeForceDeletingTheAppointmentRecord();
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    String getSuffix();
    void setSuffix(String suffix);
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public Set<String> getLanguages();
}
