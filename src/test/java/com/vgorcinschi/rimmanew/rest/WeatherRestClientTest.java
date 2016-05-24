/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest;

import com.vgorcinschi.rimmanew.rest.clients.WeatherForecastClient;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import java.net.UnknownHostException;
import java.util.Optional;
import static java.util.Optional.of;
import org.apache.logging.log4j.LogManager;
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
    private final org.apache.logging.log4j.Logger log = LogManager.getLogger();

    public WeatherRestClientTest() {
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
    public void CheckGeneralDescription() {
        assertNotNull(dwr.getDays().get(1).getSymbol().getGenerally());
        System.out.println(dwr.getDays().get(1).getSymbol().getGenerally());
    }
}
