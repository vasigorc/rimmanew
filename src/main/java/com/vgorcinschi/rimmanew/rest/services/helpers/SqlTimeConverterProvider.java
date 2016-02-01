/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.sql.Time;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author vgorcinschi
 */
@Provider
public class SqlTimeConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.getName().equals(Time.class.getName())) {
            return new ParamConverter<T>() {

                @Override
                public T fromString(String value) {
                    //in case the ParamConverter does not do URI deconding
                    value = value.replaceAll("%3A", ":");
                    if (value.length() < 6) {
                        value = value.concat(":00");
                    }
                    if (!value.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"
                            + "(:[0-5][0-9])*")) {
                        throw new BadRequestException(value + " is not an accepted Time format "
                                + "please use this pattern: hh:mm:SS");
                    }
                    return rawType.cast(Time.valueOf(value));
                }

                @Override
                public String toString(T value) {
                    // if (value instanceof java.sql.Time) {
                    Time timeRepr = (Time) value;
                    if (timeRepr.toLocalTime().getMinute() < 10) {
                        String reply = timeRepr.toLocalTime().getHour()+":"
                                +timeRepr.toLocalTime().getMinute();
                        return reply.concat("0");
                    }
                    return timeRepr.toLocalTime().toString();
                    //}
                    //return value.toString();
                }

            };
        }
        return null;
    }
}
