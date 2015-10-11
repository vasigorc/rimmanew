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
        dwr = wfc.getForecast(DailyWeatherReport.class, "fr");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void ReceivedAndUnMarshalled() {
        assertNotNull(dwr);
    }

    @Test
    public void CheckTheReturnedDate() {
        assertNotNull(dwr.getDays().get(0));
        System.out.println(dwr.getDays().get(0).getDay());
    }

    @Test
    public void CheckLocation() {
        assertEquals(dwr.getLocation().getName(), "Montreal");
    }
    
    @Test
    public void CheckGeneralDescription(){
        assertNotNull(dwr.getDays().get(1).getSymbol().getGenerally());
        System.out.println(dwr.getDays().get(1).getSymbol().getGenerally());
    }
}
