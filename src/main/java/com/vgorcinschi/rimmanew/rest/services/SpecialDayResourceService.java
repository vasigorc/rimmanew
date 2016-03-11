/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vgorcinschi.rimmanew.annotations.JpaRepository;
import com.vgorcinschi.rimmanew.ejbs.SpecialDayRepository;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.rest.services.helpers.GenericBaseJaxbListWrapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbSpecialDayListWrapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbSpecialDayListWrapperBuilder;
import static java.time.LocalDate.parse;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author vgorcinschi
 */
@Path("/specialdays")
public class SpecialDayResourceService {

    @Inject
    @JpaRepository
    private SpecialDayRepository repository;

    public void setRepository(SpecialDayRepository repository) {
        this.repository = repository;
    }

    @GET
    @Path("{date}")
    @Produces("application/json")
    public Response getSpecialDay(@PathParam("date") String date) {
        Optional<SpecialDay> specialDay = empty();
        try {
            specialDay = ofNullable(repository.getSpecialDay(parse(date)));
        } catch (DateTimeParseException | NullPointerException e) {
            if (e.getClass().getName().equals("java.lang.NullPointerException")) {
                throw new BadRequestException("You haven't provided a date",
                        Response.status(Response.Status.BAD_REQUEST).build());
            }
            throw new BadRequestException(date + " is not an accepted date format"
                    + ". Please follow this pattern: yyyy-MM-dd",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        if (specialDay.isPresent()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            String output;
            try {
                output = mapper.writeValueAsString(specialDay.get());
            } catch (JsonProcessingException ex) {
                Logger.getLogger(SpecialDayResourceService.class.getName()).log(Level.SEVERE, null, ex);
                output = "Code error serializing the appointments that you have requested";
            }
            return Response.ok(output).build();
        }
        throw new BadRequestException("Currently there is no special schedule "
                + "registered for " + date, Response.status(Response.Status.BAD_REQUEST).build());
    }

    @GET
    @Produces("application/json")
    public Response getSpecialDays(@DefaultValue("0") @QueryParam("offset") int offset,
            @DefaultValue("10") @QueryParam("size") int size) {
        Optional<List<SpecialDay>> pull = ofNullable(repository.getAll());
        //if null or empty list is returned from repository
        if (!pull.isPresent() || pull.get().isEmpty()) {
            throw new BadRequestException("Currently there are no stored schedule "
                    + "days with special schedule.", Response.status(Response.Status.BAD_REQUEST).build());
        }
        List<SpecialDay> initialList = pull.get();
        int totalDays = initialList.size();
        //how many are we returning + offset validation
        int answerSize = sizeValidator(totalDays, offset, size);
        //skip offset + limit to answerSize
        List<SpecialDay> finalList = initialList.stream().skip(offset)
                .limit(answerSize).collect(toList());
        GenericBaseJaxbListWrapper response = null;
        try {
            response = new JaxbSpecialDayListWrapperBuilder(answerSize, totalDays,
                    offset, finalList).compose();
        } catch (SecurityException | NoSuchFieldException ex) {
            Logger.getLogger(SpecialDayResourceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        //preparing our object mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String output;
        try {
            output = mapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AppointmentResourceService.class.getName()).log(Level.SEVERE, null, ex);
            output = "Code error serializing the special days that you have requested";
        }
        return Response.ok(output).build();
    }

    public int sizeValidator(int listSize, int requestOffset, int requestSize) {
        int answerSize;
        if (requestSize < 1) {
            throw new BadRequestException("You haven't requested any days with "
                    + "special schedule");
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
}
