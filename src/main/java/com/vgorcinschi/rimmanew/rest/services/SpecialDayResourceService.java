/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vgorcinschi.rimmanew.annotations.JpaRepository;
import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.ejbs.SpecialDayRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.rest.services.helpers.GenericBaseJaxbListWrapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbSpecialDayListWrapperBuilder;
import com.vgorcinschi.rimmanew.rest.services.helpers.SpecialDayCandidate;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeConverter;
import com.vgorcinschi.rimmanew.util.ExecutorFactoryProvider;
import com.vgorcinschi.rimmanew.util.InputValidators;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.appsUriBuilder;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.uriGenerator;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Long.parseLong;
import java.sql.Date;
import java.sql.Time;
import static java.time.LocalDate.parse;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import rx.Observable;

/**
 *
 * @author vgorcinschi
 */
@Path("/specialdays")
public class SpecialDayResourceService extends RimmaRestService <SpecialDay>{

    @Inject
    @JpaRepository
    private SpecialDayRepository repository;

    @Inject
    @JpaRepository
    private AppointmentRepository appointmentsRepository;

    private final org.apache.logging.log4j.Logger log = LogManager.getLogger();

    public void setRepository(SpecialDayRepository repository) {
        this.repository = repository;
    }

    public void setAppointmentsRepository(AppointmentRepository appointmentsRepository) {
        this.appointmentsRepository = appointmentsRepository;
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
        log.info("A new query for special days received. Size: " + size + " Offset: " + offset);
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
    @Consumes("application/json")
    public Response addSpecialDay(InputStream stream) {
        ObjectMapper mapper = new ObjectMapper();
        SpecialDayCandidate candidate = new SpecialDayCandidate();
        try {
            candidate = mapper.readValue(stream, SpecialDayCandidate.class);
        } catch (IOException e) {
            log.warn("Could not 'objectify' an incoming Special Day Candidate: "
                    + "" + stream.toString() + ": " + e.getMessage());
            throw new InternalServerErrorException("Could not 'objectify' an incoming Special Day Candidate: "
                    + "" + stream.toString() + ": " + e.getMessage());
        }
        log.debug("A candidate for Special Day has been received by the system: " + candidate.toString());
        String[] cantDoWithout = {candidate.getDate(), candidate.getBlocked(), candidate.getAllowConflicts()};
        if (!InputValidators.allStringsAreGood.apply(cantDoWithout)) {
            throw new BadRequestException("You forgot to enter either the special schedule's "
                    + "day (format: yyyy-MM-dd), whether it is or it is not a "
                    + "blocked day or whether you permit this schedule day to "
                    + "conflict with the existing appointments (both true or"
                    + " false). These fields are mandatory -please try again.",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        Date sdDate = new SqlDateConverter().fromString(candidate.getDate());
        CompletableFuture<Optional<List<Appointment>>> conflictingAppointments
                = CompletableFuture.supplyAsync(
                        () -> {
                            return ofNullable(appointmentsRepository.getByDate(sdDate));
                        }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        /*
         Special Schedule Days are intended to be unique. So if there is a
         Special Schedule Day on the date that was requested by the user
         we will notify them of that and invite to use the update method instead
         */
        CompletableFuture<Optional<SpecialDay>> oldSd
                = CompletableFuture.supplyAsync(() -> {
                    //safe to use the passed 'appDate' param from the user - already validated
                    //above
                    return ofNullable(repository.getSpecialDay(sdDate.toLocalDate()));
                },
                ExecutorFactoryProvider.getSingletonExecutorOf30());
        SpecialDay newbie;
        try {
            //obtain the new SpecialDay if no 400 Exception is not thrown
            newbie = checkAndBuild(sdDate, candidate.getStartAt(),
                    candidate.getEndAt(), candidate.getBreakStart(),
                    candidate.getBreakEnd(), candidate.getDuration(),
                    candidate.getBlocked(), candidate.getMessage());
        } catch (Exception e) {
            //re-throw the 400 exception after cancelling the futures
            conflictingAppointments.cancel(true);
            oldSd.cancel(true);
            throw e;
        }
        Optional<List<Appointment>> conflicts = empty();
        //extract our Futures and verify whether there is an existing special day
        try {
            Optional<SpecialDay> oldSpecialDay = oldSd.get(1, TimeUnit.SECONDS);
            /*
             If there is a Special Day on the requested date - inform the
             user of the next steps
             */
            if (oldSpecialDay.isPresent()) {
                conflictingAppointments.getNow(null);
                throw new BadRequestException("There is already a special schedule "
                        + "on " + candidate.getDate() + ". Find and modify it "
                        + "if you wish to update it.",
                        Response.status(Response.Status.BAD_REQUEST).build());
            } else {
                //else our next step is to start the 'check conflicting appointments
                //workflow
                conflicts = conflictingAppointments.get(500, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(SpecialDayResourceService.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to perform server side validation. "
                    + "Please contact the support team;");
        }
        if (conflicts.isPresent() && conflicts.get().size() > 0
                && !candidate.getAllowConflicts().equals("true")) {
            /*
             Make the user aware that there may be conflicts with the existing
             appointments.
             */
            throw new BadRequestException("Please note that there is(are) currently "
                    + conflicts.get().size() + " appointment(s) on the date that you"
                    + " have chosen. Check the corresponding check-box to confirm "
                    + "that you still want to save this day.",
                    Response.status(Response.Status.BAD_REQUEST).build());
        } else {
            repository.setSpecialDay(newbie);
            //parameters for the return link
            Map<String, String> map = new HashMap<>();
            map.put("path", "specialdays/" + newbie.getDate().toString());
            return Response.ok(
                    getJsonRepr("link", uriGenerator.apply(appsUriBuilder, map).toASCIIString())).build();
        }
    }

    @PUT
    @Path("{date}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateSpecialDay(@PathParam("date") String date, InputStream stream) {
        ObjectMapper mapper = new ObjectMapper();
        SpecialDayCandidate candidate = new SpecialDayCandidate();
        //return 500 if serialization fails
        try {
            candidate = mapper.readValue(stream, SpecialDayCandidate.class);
        } catch (IOException e) {
            log.warn("Could not 'objectify' an incoming Special Day Candidate: "
                    + "" + stream.toString() + ": " + e.getMessage());
            throw new InternalServerErrorException("Could not 'objectify' an incoming Special Day Candidate: "
                    + "" + stream.toString() + ": " + e.getMessage());
        }
        log.info("New special day candidate: "+candidate);
        String[] cantDoWithout = {candidate.getDate(), candidate.getBlocked(), candidate.getAllowConflicts()};
        if (!InputValidators.allStringsAreGood.apply(cantDoWithout)) {
            throw new BadRequestException("You forgot to enter either the special schedule's "
                    + "day (format: yyyy-MM-dd), whether it is or it is not a "
                    + "blocked day or whether you permit this schedule day to "
                    + "conflict with the existing appointments (both true or"
                    + " false). These fields are mandatory -please try again.",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        Date sdDate = new SqlDateConverter().fromString(candidate.getDate());
        //checking for conflicting appointments
        CompletableFuture<Optional<List<Appointment>>> conflictingAppointments
                = CompletableFuture.supplyAsync(
                        () -> {
                            return ofNullable(appointmentsRepository.getByDate(sdDate));
                        }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        /*
         we don't immediately check if the requested appDate exists or not
         in order to prevent the legal sdDate harvesting by measuring the time
         difference in responses between entering valid/invalid appDates
         */
        CompletableFuture<Optional<SpecialDay>> oldSd
                = CompletableFuture.supplyAsync(() -> {
                    return ofNullable(repository.getSpecialDay(sdDate.toLocalDate()));
                },
                ExecutorFactoryProvider.getSingletonExecutorOf30());
        SpecialDay newDay = null;
        try {
            newDay = checkAndBuild(sdDate, candidate.getStartAt(), candidate.getEndAt(),
                    candidate.getBreakStart(), candidate.getBreakEnd(),
                    candidate.getDuration(), candidate.getBlocked(), candidate.getMessage());
        } catch (Exception e) {
            //cancelling existing Futures - tested successfully
            oldSd.cancel(true);
            conflictingAppointments.cancel(true);
            throw e;
        }
        //this line is not reached unless SpecialDay newDay is valid
        //only now we check whether the requested date exists and throw a 
        //400 error with a corresponding message
        try {
            Optional<SpecialDay> oldSpecialDay = oldSd.get(1, TimeUnit.SECONDS);
            if (!oldSpecialDay.isPresent()) {
                //forcing to complete the remaining Future. Using this instead
                //of the cancel() method - the later isn't working in these
                //circumstances - a known bug - getNow() tested successfully
                conflictingAppointments.getNow(null);
                throw new BadRequestException("There is no special schedule "
                        + "on " + candidate.getDate() + ". So it is hard to update it. You can consider "
                        + "creating one instead.",
                        Response.status(Response.Status.BAD_REQUEST).build());
            } else {
                //CHECK FOR CONFLICTING APPOINTMENTS!!!
                Optional<List<Appointment>> conflicts = empty();
                try {
                    conflicts = conflictingAppointments.get(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    Logger.getLogger(SpecialDayResourceService.class.getName()).log(Level.SEVERE, null, ex);
                    throw new InternalServerErrorException("It took the application "
                            + "too long check for the conflicts with the"
                            + " existing appointments. Please contact the support team;");
                }
                if (conflicts.isPresent() && conflicts.get().size() > 0
                        && !candidate.getAllowConflicts().equals("true")) {
                    /*
                     Make the user is aware that there may be conflicts with the existing
                     appointments.
                     */
                    throw new BadRequestException("Please note that there is(are) currently "
                            + conflicts.get().size() + " appointment(s) on " + candidate.getDate()
                            + ". Check the corresponding check-box to confirm "
                            + "that you still want to save this day.",
                            Response.status(Response.Status.BAD_REQUEST).build());
                } else {
                    /*
                     If we're here - we can safely (with sincere heart)
                     So that our JPA doesn't complain we need to add the id of the
                     oldSpecialDay to the newDay and merge :-)
                     */
                    newDay.setId(oldSpecialDay.get().getId());
                    repository.updateSpecialDay(newDay);
                    //send back 200 response along with the resource address of the
                    //updated item
                    Map<String, String> map = new HashMap<>();
                    map.put("path", "specialdays/" + newDay.getDate().toString());
                    return Response.ok(
                            getJsonRepr("link", uriGenerator.apply(appsUriBuilder, map).toASCIIString())).build();
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(SpecialDayResourceService.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("It took the application "
                    + "too long to query the database. Please try again or signal "
                    + "the problem to the support team.");
        }
    }

    @DELETE
    @Path("{date}")
    @Produces("application/json")
    public Response deleteSpecialDay(@PathParam("date") String appDate,
            @DefaultValue("false") @QueryParam("allowConflicts") String allowConflicts) {
        String[] cantDoWithout = {appDate, allowConflicts};
        if (!InputValidators.allStringsAreGood.apply(cantDoWithout)) {
            throw new BadRequestException("You forgot to enter either the special schedule's "
                    + "day (format: yyyy-MM-dd), or whether you permit this schedule day to "
                    + "conflict with the existing appointments (true or"
                    + " false). Both are mandatory -please try again.",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        Date sdDate = new SqlDateConverter().fromString(appDate);
        //checking for conflicting appointments
        CompletableFuture<Optional<List<Appointment>>> conflictingAppointments
                = CompletableFuture.supplyAsync(
                        () -> {
                            return ofNullable(appointmentsRepository.getByDate(sdDate));
                        }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        /*
         same as in addSpecialDay method
         we do not immediately check if the requested appDate exists or not
         in order to prevent the legal appDate harvesting by measuring the time
         difference in responses between entering valid/invalid appDates
         */

        Optional<SpecialDay> oldSpecialDay = ofNullable(repository.getSpecialDay(parse(appDate)));
        if (!oldSpecialDay.isPresent()) {
            //forcing to complete the remaining Future. Using this instead
            //of the cancel() method - the later isn't working in these
            //circumstances - a known bug - getNow() tested successfully
            conflictingAppointments.getNow(null);
            throw new BadRequestException("There is no special schedule "
                    + "on " + appDate + ". So it is hard to delete it. ",
                    Response.status(Response.Status.BAD_REQUEST).build());
        } else {
            //Checking for conflicting appointments
            Optional<List<Appointment>> conflicts = empty();
            try {
                conflicts = conflictingAppointments.get(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                Logger.getLogger(SpecialDayResourceService.class.getName()).log(Level.SEVERE, null, ex);
                throw new InternalServerErrorException("It took the application "
                        + "too long check for the conflicts with the"
                        + " existing appointments. Please contact the support team;");
            }
            if (conflicts.isPresent() && conflicts.get().size() > 0
                    && !allowConflicts.equals("true")) {
                /*
                 Make the user is aware that there may be conflicts with the existing
                 appointments.
                 */
                throw new BadRequestException("Please note that there is(are) currently "
                        + conflicts.get().size() + " appointment(s) on " + appDate
                        + ". Check the corresponding check-box to confirm "
                        + "that you still want to delete this day.",
                        Response.status(Response.Status.BAD_REQUEST).build());
            } else {
                if (repository.deleteSpecialDay(oldSpecialDay.get())) {
                    return Response.ok(getJsonRepr("response", "The special schedule "
                            + "on " + appDate + " has been deleted")).build();
                } else {
                    throw new InternalServerErrorException("Due to a server "
                            + "error the special schedule day on " + appDate
                            + " could not be deleted.",
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                }
            }
        }

    }

    public SpecialDay checkAndBuild(Date sdDate, String startAt, String endAt, String breakStart,
            String breakEnd, String duration, String blocked, String message) {
        //instantiate a SpecialDay and populate while validate
        SpecialDay sd = new SpecialDay();
        sd.setDate(sdDate);
        //second but almost equaly important is - "Is this a closed day or not?"
        if (InputValidators.stringNotNullNorEmpty.apply(blocked) && (blocked.equalsIgnoreCase("true") || blocked.equalsIgnoreCase("yes"))) {
            //no further info is needed - return the object to the caller
            sd.setIsBlocked(true);
            return sd;
        } else {
            //regardless of balderdash that the user (not) entered we will set it to false
            sd.setIsBlocked(false);
        }
        //now validate start and end times - throws BadRequestException
        String[] startAndEnd = {startAt, endAt};
        if (!InputValidators.allStringsAreGood.apply(startAndEnd)) {
            throw new BadRequestException("You manifested that this is a "
                    + "special schedule day and that it is not a blocked day, but you haven't "
                    + "provided the start and end date of this day. Please provide both. "
                    + "The required time format is: HH:ss",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
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
            String durationErrorMessage;
            if (!InputValidators.stringNotNullNorEmpty.apply(duration)) {
                durationErrorMessage = "You haven't entered a duration. ";
            } else {
                durationErrorMessage = duration + " is not an accepted Duration format. ";
            }
            throw new BadRequestException(durationErrorMessage + "Please enter the "
                    + "number of minutes. Ex: 45",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        //remaining fields are all optional
        //break times need to be BOTH valid only if specified
        String[] breaks = {breakStart, breakEnd};
        if (InputValidators.allStringsAreGood.apply(breaks)) {
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
        if (InputValidators.stringNotNullNorEmpty.apply(message)) {
            sd.setMessage(message);
        }
        return sd;
    }

    @Override
    protected JsonObject entityToJson(SpecialDay entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Observable<SpecialDay> rxEntityList(List<SpecialDay> l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}