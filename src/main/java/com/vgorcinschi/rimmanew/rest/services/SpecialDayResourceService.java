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
import static java.time.LocalDate.parse;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    public Response getSpecialDays() {
        return null;
    }
}
