package com.vgorcinschi.rimmanew.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.entities.Groups;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.rest.services.helpers.GenericBaseJaxbListWrapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbAppointmentListWrapperBuilder;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbGroupsListWrapperBuilder;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbSpecialDayListWrapperBuilder;
import java.util.List;
import javaslang.control.Try;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Produces;

/**
 *
 * @author vgorcinschi
 * @param <A> - model for which this resource will be a support
 */
@Produces("application/json")
public abstract class RimmaRestService <A>{
    
    protected ObjectMapper getMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
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
}