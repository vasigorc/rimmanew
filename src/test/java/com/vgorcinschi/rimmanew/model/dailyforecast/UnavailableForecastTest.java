/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model.dailyforecast;

import com.vgorcinschi.rimmanew.model.LegacyWeatherForecastBean;
import com.vgorcinschi.rimmanew.rest.WeatherForecastClient;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author vgorcinschi
 */
public class UnavailableForecastTest {
    private final WeatherForecastClient wfc;
    private final DailyWeatherReport dwr;
    private final LegacyWeatherForecastBean weatherbean;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public UnavailableForecastTest() {
        wfc = new WeatherForecastClient();
        dwr = wfc.getForecast(DailyWeatherReport.class, "fr");
        this.weatherbean = new LegacyWeatherForecastBean();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testADateFarInTheFuture() throws ParseException{
        Date aDate = sdf.parse("2019-01-01");
        weatherbean.update(aDate);
        assertThat("Becase we are passing to the"
                + " update method a date which is outside the 15' days "
                + "lag, we expect that the DailyForecast instance of the weatherBean "
                + "will be of class UnavailableForecast",
                weatherbean.getForecast(), instanceOf(UnavailableForecast.class));
        System.out.println(weatherbean.getForecast().getGenerally());
    }    
}
