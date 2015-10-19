/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model.dailyforecast;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import java.time.LocalDate;
import static java.time.LocalDate.parse;
import java.time.format.TextStyle;
import java.util.Locale;

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
        return time.getClouds().getValue().substring(0, 1).toUpperCase()+time.getClouds().getValue().substring(1);
    }

    @Override
    public String getGenerally() {
        return time.getSymbol().getGenerally().substring(0, 1).toUpperCase()+time.getSymbol().getGenerally().substring(1);
    }

    @Override
    public String getIconUrl() {
        return time.getSymbol().getPicLink();
    }

    @Override
    public String getWind() {
        return time.getWindSpeed().getWindDescr();
    }

    @Override
    public String getDay() {
        //TODO write code that will differ based on current Locale
        LocalDate sourceDate = parse(time.getDay());
        String month = sourceDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH);
        month = month.replace(".", ",");
        //do some substringing to the month part to capitalize the first letter
        return sourceDate.getDayOfMonth()+" "+month.substring(0, 1).toUpperCase()+month.substring(1)+" "+sourceDate.getYear();
    }
    
}
