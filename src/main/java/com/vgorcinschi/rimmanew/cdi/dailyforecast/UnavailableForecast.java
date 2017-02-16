/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi.dailyforecast;

/**
 *
 * @author vgorcinschi
 */
public class UnavailableForecast implements DailyForecast{

    private String reason="";

    public UnavailableForecast setReason(String reason) {
        this.reason = reason;
        return this;
    }
    
    @Override
    public String getMaxT() {
        return "";
    }

    @Override
    public String getMinT() {
        return "";
    }

    @Override
    public String getHumidity() {
        return "";
    }

    @Override
    public String getClouds() {
        return "";
    }

    @Override
    public String getGenerally() {
        /*
            Do some work for text customization according to the 
        current locale
        */
        return this.reason;
    }

    @Override
    public String getIconUrl() {
        return "";
    }

    @Override
    public String getWind() {
        return "";
    }

    @Override
    public String getDay() {
        return "";
    }
    
}
