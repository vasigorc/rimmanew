/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.sql.Date;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author vgorcinschi
 */
@Provider
public class SqlDateConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.getName().equals(Date.class.getName())) {
            return new ParamConverter<T>() {

                @Override
                public T fromString(String value) {
                    if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        throw new BadRequestException(value + " wasn't recognized as "
                                + "a valid date on the server side. Please follow this "
                                + "pattern: yyyy-mm-dd");
                    }
                    return rawType.cast(Date.valueOf(value));
                }

                @Override
                public String toString(T value) {
                    if (value==null) {
                        return null;
                    }
                    return value.toString();
                }

            };
        }
        return null;
    }

}
