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
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbSpecialDayListWrapperBuilder;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeConverter;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.allStringsAreGood;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.stringNotNullNorEmpty;
import static java.lang.Long.parseLong;
import java.sql.Date;
import java.sql.Time;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response addSpecialDay(@FormParam("date") String appDate, @FormParam("start") String startAt,
            @FormParam("end") String endAt, @FormParam("breakStart") String breakStart,
            @FormParam("breakEnd") String breakEnd, @FormParam("duration") String duration,
            @FormParam("blocked") String blocked, @FormParam("message") String message) {
        /*
            MUST CONTAIN LOGIC TO WARN THAT THERE ARE ALREADY APPOINTMENTS IN THIS
            DAY
        */
        return null;
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

    public SpecialDay checkAndBuild(String appDate, String startAt, String endAt, String breakStart,
            String breakEnd, String duration, String blocked, String message) {
        //instantiate a SpecialDay and populate while validate
        SpecialDay sd = new SpecialDay();
        //first we need to validate the date -without it everything else is 
        //irrelevant. Throws BadRequestException
        Date sdDate = new SqlDateConverter().fromString(appDate);
        sd.setDate(sdDate);
        //second but almost equaly important is - "Is this a closed day or not?"
        if (stringNotNullNorEmpty.apply(blocked) && (blocked.equalsIgnoreCase("true") || blocked.equalsIgnoreCase("yes"))) {
            //no further info is needed - return the object to the caller
            sd.setIsBlocked(true);
            return sd;
        } else {
            //regardless of balderdash that the user (not) entered we will set it to false
            sd.setIsBlocked(false);
        }
        //now validate start and end times - throws BadRequestException
        Time sdStart = new SqlTimeConverter().fromString(startAt);
        Time sdEnd = new SqlTimeConverter().fromString(endAt);
        //end cannot be before start or even equal to...
        if (!sdEnd.after(sdStart)) {
            throw new BadRequestException("You have entered the "
                    + "end time: " + endAt + ", to be before/equal to the start time:" + startAt + "."
                    + " Please make sure that the end is after the start \u263A",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        sd.setStartAt(sdStart);
        sd.setEndAt(sdEnd);
        //duration is the last field without which we cannot save a day with
        //a special schedule
        try {
            long sdDuration = parseLong(duration);
            sd.setDuration(sdDuration);
        } catch (NumberFormatException e) {
            throw new BadRequestException(duration + " is not an accepted Duration format "
                    + "please enter the number of minutes. Ex: 45",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        //remaining fields are all optional
        //break times need to be BOTH valid only if specified
        String[] breaks = {breakStart, breakEnd};
        if (allStringsAreGood.apply(breaks)) {
            Time stBreakStart = new SqlTimeConverter().fromString(breakStart);
            Time stBreakEnd = new SqlTimeConverter().fromString(breakEnd);
            //again the end cannot be after the start
            if (!stBreakEnd.after(stBreakStart)) {
                throw new BadRequestException("You have entered the "
                        + "break end time: " + breakEnd + ", to be before/equal "
                        + "to the break start time:" + breakStart + "."
                        + " Please make sure that the end is after the start \u263A",
                        Response.status(Response.Status.BAD_REQUEST).build());
            }
            sd.setBreakStart(stBreakStart);
            sd.setBreakEnd(stBreakEnd);
        } else {
            sd.setBreakStart(null);
            sd.setBreakEnd(null);
        }
        if(stringNotNullNorEmpty.apply(message))
            sd.setMessage(message);
        return sd;
    }
}
