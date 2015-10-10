/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model.dailyforecast;

/**
 *
 * @author vgorcinschi
 */
public class UnavailableForecast implements DailyForecast{

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
        return "Unfortunatelly we are unable to provide weather forecast for that"
                + " far in the future.";
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
