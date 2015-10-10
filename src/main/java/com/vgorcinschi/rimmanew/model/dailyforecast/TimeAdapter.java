/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model.dailyforecast;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;

/**
 *
 * @author vgorcinschi
 */
public class TimeAdapter implements DailyForecast{
    private final Time time;

    public TimeAdapter(Time time) {
        this.time = time;
    }    
    
    @Override
    public String getMaxT() {
        return time.getTemperature().getMax();
    }

    @Override
    public String getMinT() {
        return time.getTemperature().getMin();
    }

    @Override
    public String getHumidity() {
        return time.getHumidity().getValue();
    }

    @Override
    public String getClouds() {
        return time.getClouds().getValue();
    }

    @Override
    public String getGenerally() {
        return time.getSymbol().getGenerally();
    }

    @Override
    public String getIconUrl() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getWind() {
        return time.getWindSpeed().getWindDescr();
    }

    @Override
    public String getDay() {
        return time.getDay();
    }
    
}
