/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest;

import com.vgorcinschi.rimmanew.rest.clients.WeatherForecastClient;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Clouds;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Humidity;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Temperature;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import java.net.UnknownHostException;
import java.util.Optional;
import static java.util.Optional.of;
import org.apache.logging.log4j.LogManager;
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
    private final org.apache.logging.log4j.Logger log = LogManager.getLogger();

    public TimeFieldsTest() {
        wfc = new WeatherForecastClient();
        Optional<DailyWeatherReport> temporary = Optional.empty();
        try {
            temporary = of(wfc.getForecast(DailyWeatherReport.class, "fr"));
        } catch (UnknownHostException|javax.ws.rs.ProcessingException ex) {
            log.warn("No access to the third party weather service: " + ex.getMessage());
        }
        if (temporary.isPresent()) {
            dwr = temporary.get();
        } else {
            dwr = null;
        }
    }

    @Before
    public void setUp() {
        org.junit.Assume.assumeTrue(dwr != null);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void MaxTempIsBiggerThenMin() {
        time = dwr.getDays().get(3);
        Temperature temp = time.getTemperature();
        assertTrue(Double.parseDouble(temp.getMax()) >= Double.parseDouble(temp.getMin()));
    }

    @Test
    public void TestHumidityIsWithinOneHundred() {
        time = dwr.getDays().get(3);
        Humidity humidity = time.getHumidity();
        assertThat(Integer.parseInt(humidity.getValue()), greaterThanOrEqualTo(0));
        assertThat("Humidity value must not be greater than 100",
                Integer.parseInt(humidity.getValue()), lessThanOrEqualTo(100));
    }

    @Test
    public void TestThatCloudsIsString() {
        time = dwr.getDays().get(3);
        Clouds clouds = time.getClouds();
        System.out.println(clouds.getValue());
        assertThat("Clouds description should be of class String",
                clouds.getValue(), instanceOf(String.class));
    }
}
