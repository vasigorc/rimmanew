/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.sql.Time;
import javax.ws.rs.ext.ParamConverter;

/**
 *
 * @author vgorcinschi
 */
public class SqlTimeConverter implements ParamConverter<Time>{

    @Override
    public Time fromString(String value) {
        return Time.valueOf(value);
    }

    @Override
    public String toString(Time value) {
        return value.toLocalTime().getHour()+":"+value.toLocalTime().getMinute();
    }
    
}
