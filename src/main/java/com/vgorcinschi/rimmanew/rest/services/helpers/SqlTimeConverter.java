/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.sql.Time;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.ParamConverter;

/**
 *
 * @author vgorcinschi
 */
public class SqlTimeConverter implements ParamConverter<Time> {

    private final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]"
            + "(:[0-5][0-9])*";

    @Override
    public Time fromString(String value) {
        //in case the ParamConverter does not do URI deconding
        value = value.replaceAll("%3A", ":");
        //in case if the client has not sent the seconds we still need to 
        //append them so that the Constructor works
        if (value.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]") && value.length() < 6) {
            value = value.concat(":00");
        }
        if (!value.matches(TIME24HOURS_PATTERN)) {
            throw new BadRequestException(value + " is not an accepted Time format "
                    + "please use this pattern: hh:mm:SS");
        }
        return Time.valueOf(value);
    }

    @Override
    public String toString(Time value) {
        if (value.toLocalTime().getMinute()<9) {
            return value.toLocalTime().getHour() + ":" + value.toLocalTime().getMinute()+"0";
        }
        return value.toLocalTime().getHour() + ":" + value.toLocalTime().getMinute();
    }

}
