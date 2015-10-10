/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model.dailyforecast;

import com.vgorcinschi.rimmanew.model.WeatherForecastBean;
import com.vgorcinschi.rimmanew.rest.WeatherForecastClient;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class AvailableForecastTest {
    private final WeatherForecastClient wfc;
    private final DailyWeatherReport dwr;
    private final WeatherForecastBean weatherbean;
    private java.util.Date alwaysNextWeek;
    
    public AvailableForecastTest() {
        wfc = new WeatherForecastClient();
        dwr = wfc.getForecast(DailyWeatherReport.class, "fr");
        this.weatherbean = new WeatherForecastBean();
    }
    
    @Before
    public void setUp() {
        //setting-up the tests for a day in a week from now
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +7); 
        alwaysNextWeek = cal.getTime();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testThatATimeAdapterIsCreated(){
        weatherbean.update(alwaysNextWeek);
        assertThat("As we are passing the date within "
                + "the expected 15's days range, we will have "
                + "to end-up with a TimeAdapter implementation "
                + "of DailyForecast.",
                weatherbean.getForecast(), instanceOf(TimeAdapter.class));
        System.out.println(weatherbean.getForecast().getGenerally());
    }
}
