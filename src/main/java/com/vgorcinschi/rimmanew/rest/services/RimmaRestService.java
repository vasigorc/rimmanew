package com.vgorcinschi.rimmanew.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.entities.Groups;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.rest.services.helpers.GenericBaseJaxbListWrapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbAppointmentListWrapperBuilder;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbGroupsListWrapperBuilder;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbSpecialDayListWrapperBuilder;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javaslang.control.Try;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Produces;
import rx.Observable;

/**
 *
 * @author vgorcinschi
 * @param <A> - model for which this resource will be a support
 */
@Produces("application/json")
public abstract class RimmaRestService <A>{
    
    //for the entityToJson method
    protected final Map<String, Object> configs = new HashMap<>(1);
    protected final JsonBuilderFactory factory;
    DateTimeFormatter formatter =
    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                     .withLocale( Locale.CANADA )
                     .withZone( ZoneId.systemDefault() );

    public RimmaRestService() {
        this.configs.put(JsonGenerator.PRETTY_PRINTING, true);
        this.factory = Json.createBuilderFactory(configs);
    }
    
    protected ObjectMapper getMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        mapper.enableDefaultTyping();
        return mapper;
    }    
    
    protected int sizeValidator(int listSize, int requestOffset, int requestSize) {
        int answerSize;
        if (requestSize < 1) {
            throw new BadRequestException("You haven't requested any entries");
        }
        if ((listSize - requestOffset) < 0) {
            throw new BadRequestException("There are less entries in the "
                    + "system than you have requested");
        }
        if ((listSize - requestOffset) < requestSize) {
            answerSize = listSize - requestOffset;
        } else {
            answerSize = requestSize;
        }
        return answerSize;
    }
    
    protected Try<GenericBaseJaxbListWrapper> listWrapperFactory(
            String type, int answerSize, 
            int totalMatches, int offset, List<A> finalList){
        switch(type.trim().toLowerCase()){
            case "appointment": return Try.of(()->
            new JaxbAppointmentListWrapperBuilder(answerSize, totalMatches,
                            offset, (List<Appointment>) finalList).compose());
            case "specialday": return Try.of(()->
            new JaxbSpecialDayListWrapperBuilder(answerSize, totalMatches,
            offset, (List<SpecialDay>) finalList).compose());
            case "groups": return Try.of(()->
            new JaxbGroupsListWrapperBuilder(answerSize, totalMatches, 
                    offset, (List<Groups>) finalList).compose());
            default: throw new UnsupportedOperationException(type+" doesn't have"
                    + " a custom list wrapper");
        }
    }
    
    protected abstract JsonObject entityToJson(A entity);
    
    protected abstract Observable<A> rxEntityList(List<A> l);
    
    public String getJsonRepr(String key, String value) {
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        ObjectNode object = jsonNodeFactory.objectNode();
        object.put(key, value);
        return object.toString();
    }
}