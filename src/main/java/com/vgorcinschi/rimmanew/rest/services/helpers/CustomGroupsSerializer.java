package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vgorcinschi.rimmanew.entities.Groups;
import java.io.IOException;

/**
 *
 * @author vgorcinschi
 */
public class CustomGroupsSerializer extends JsonSerializer<Groups>{

    @Override
    public void serialize(Groups t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeString(t.getGroupName());//just returning the group name
    }
    
}
