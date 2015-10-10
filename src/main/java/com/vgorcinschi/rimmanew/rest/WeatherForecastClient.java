/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.CustomForecastUnmarshaller;
import java.io.Closeable;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author vgorcinschi
 */
public class WeatherForecastClient implements Closeable {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = "http://api.openweathermap.org/data/2.5/forecast/daily";

    public WeatherForecastClient() {
        this.client = javax.ws.rs.client.ClientBuilder.newBuilder().register(CustomForecastUnmarshaller.class).build();
        this.webTarget = client.target(BASE_URI);
    }

    public <T> T getForecast(Class<T> responseType, String language) {
        WebTarget resource = webTarget.queryParam("APPID","edc67756a4c4adae15d281fe52adb26e")
                .queryParam("q", "Montreal")
                .queryParam("mode", "xml").queryParam("units", "metric")
                .queryParam("cnt", "16")
                .queryParam("lang", language);
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

}
