/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import static com.vgorcinschi.rimmanew.util.InputValidators.stringIsValidDate;
import java.sql.Date;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ParamConverter;

/**
 *
 * @author vgorcinschi
 */
public class SqlDateConverter implements ParamConverter<Date> {   
    
    @Override
    public Date fromString(String value) {
        if (!value.matches("\\d{4}-\\d{2}-\\d{2}") || !stringIsValidDate.apply(value)) {                    
            throw new BadRequestException(value + " wasn't recognized as "
                    + "a valid date on the server side. Please follow this "
                    + "pattern: yyyy-mm-dd", 
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        return Date.valueOf(value);
    }

    @Override
    public String toString(Date value) {
        return value.toLocalDate().toString();
    }

}
