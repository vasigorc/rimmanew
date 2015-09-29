/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Temperature;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class TimeFieldsTest {
    private final WeatherForecastClient wfc;
    private final DailyWeatherReport dwr;
    private Time time;
    
    public TimeFieldsTest() {
        wfc = new WeatherForecastClient();
        dwr = wfc.getForecast(DailyWeatherReport.class, "fr");
    }
    
    @Before
    public void setUp() {
        time = dwr.getDays().get(2);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void CheckWindDescription(){        
        String windName= time.getWindSpeed().getWindDescr();
        assertTrue(windName.contains("Breeze"));
    }
    
    @Test
    public void MaxTempIsBiggerThenMin(){
        Temperature temp = time.getTemperature();
        assertTrue(Double.parseDouble(temp.getMax())>=Double.parseDouble(temp.getMin()));
    }
}
