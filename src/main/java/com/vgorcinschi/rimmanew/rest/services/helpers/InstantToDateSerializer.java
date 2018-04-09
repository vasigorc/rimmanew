package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author vgorcinschi
 */
public class InstantToDateSerializer extends JsonSerializer<Instant>{

    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    
    @Override
    public void serialize(Instant t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        Date.from(t).toString();
    }
    
}
