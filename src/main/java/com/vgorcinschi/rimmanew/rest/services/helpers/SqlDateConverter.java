/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.sql.Date;
import javax.ws.rs.ext.ParamConverter;

/**
 *
 * @author vgorcinschi
 */
public class SqlDateConverter implements ParamConverter<Date> {

    @Override
    public Date fromString(String value) {
        return Date.valueOf(value);
    }

    @Override
    public String toString(Date value) {
        return value.toLocalDate().toString();
    }

}
