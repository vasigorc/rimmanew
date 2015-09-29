/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

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
    
    @Test
    public void CheckWindDescription(){
        Pattern pattern = Pattern.compile("\bBreeze\b");
        String windName= dwr.getDays().get(2).getWindSpeed().getWindDescr();
        Matcher windNameMatcher = pattern.matcher(windName);
        assertTrue(windNameMatcher.matches());
    }
}
