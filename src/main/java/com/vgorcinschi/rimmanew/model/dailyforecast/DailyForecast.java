/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model.dailyforecast;

/**
 *
 * @author vgorcinschi
 */
public interface DailyForecast {

    String getMaxT();

    String getMinT();

    String getHumidity();

    String getClouds();

    String getGenerally();

    String getIconUrl();

    String getWind(); //WindSpeed.windDescr

    String getDay();
}
