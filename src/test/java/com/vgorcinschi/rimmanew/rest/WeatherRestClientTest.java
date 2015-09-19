/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author vgorcinschi
 */
public class WeatherRestClientTest {
    private WeatherForecastClient wfc;
    private DailyWeatherReport dwr;
    
    public WeatherRestClientTest() {
    }
    
    @Before
    public void setUp() {
        wfc = new WeatherForecastClient();
    }
    
    @After
    public void tearDown() {
    }

    @Test
     public void ReceivedAndUnMarshalled() {
         dwr = wfc.getForecast(DailyWeatherReport.class, "fr");
         assertNotNull(dwr);
     }
}
