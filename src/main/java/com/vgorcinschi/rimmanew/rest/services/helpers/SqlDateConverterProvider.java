/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.sql.Date;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

/**
 *
 * @author vgorcinschi
 */
public class SqlDateConverterProvider implements ParamConverterProvider {

    private final SqlDateConverter converter = new SqlDateConverter();

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (!rawType.equals(Date.class))
            return null;
        return (ParamConverter<T>) converter;
    }

}
