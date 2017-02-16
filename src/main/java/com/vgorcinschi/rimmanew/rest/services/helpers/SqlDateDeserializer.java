/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import static com.vgorcinschi.rimmanew.util.InputValidators.stringIsValidDate;
import java.io.IOException;
import java.sql.Date;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

/**
 *
 * @author vgorcinschi
 */
public class SqlDateDeserializer extends JsonDeserializer<Date>{

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        String value = jp.getText();
        if (!value.matches("\\d{4}-\\d{2}-\\d{2}") || !stringIsValidDate.apply(value)) {                    
            throw new BadRequestException(value + " wasn't recognized as "
                    + "a valid date on the server side. Please follow this "
                    + "pattern: yyyy-mm-dd", 
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        return Date.valueOf(value);
    }
    
}
