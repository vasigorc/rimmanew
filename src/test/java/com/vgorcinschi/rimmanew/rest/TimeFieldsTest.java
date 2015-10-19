/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.Clouds;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Humidity;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Temperature;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

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
        time = dwr.getDays().get(3);
    }
    
    @After
    public void tearDown() {
    }
   
    @Test
    public void MaxTempIsBiggerThenMin(){
        Temperature temp = time.getTemperature();
        assertTrue(Double.parseDouble(temp.getMax())>=Double.parseDouble(temp.getMin()));
    }
    
    @Test
    public void TestHumidityIsWithinOneHundred(){
        Humidity humidity = time.getHumidity();
        assertThat(Integer.parseInt(humidity.getValue()), greaterThanOrEqualTo(0));
        assertThat("Humidity value must not be greater than 100", 
                Integer.parseInt(humidity.getValue()), lessThanOrEqualTo(100));
    }
    
    @Test
    public void TestThatCloudsIsString(){
        Clouds clouds = time.getClouds();
        System.out.println(clouds.getValue());
        assertThat("Clouds description should be of class String",
                clouds.getValue(), instanceOf(String.class));
    }
}
