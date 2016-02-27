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
import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbAppointmentListWrapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbAppointmentListWrapperBuilder;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeConverter;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidate;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidatesTriage;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QueryCommandControl;
import com.vgorcinschi.rimmanew.util.ExecutorFactoryProvider;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.appsUriBuilder;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.uriGenerator;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author vgorcinschi
 */
@Path("/appointments")
public class AppointmentResourceService {

    @Inject
    @JpaRepository
    private AppointmentRepository repository;

    public void setRepository(AppointmentRepository repository) {
        this.repository = repository;
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getAppointment(@PathParam("id") long id) {
        Appointment reply = repository.get(id);
        if (reply == null) {
            throw new NotFoundException("The appointment with id " + id + " could"
                    + " not be found");
        }
        ResponseBuilder builder = Response.ok(reply);
        return builder.language(Locale.CANADA_FRENCH).build();
    }

    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response bookAppointment(@FormParam("date") Date appDate,
            @FormParam("time") String appTime, @FormParam("type") String appType,
            @FormParam("clientName") String clientName, @FormParam("email") String clientEmail,
            @DefaultValue("") @FormParam("message") String clientMsg) {
        //externalize the validation of all fields to concentrate on "positive"
        //scenario only
        validator(appDate, appType, clientName, clientEmail);
        Time converted = new SqlTimeConverter().fromString(appTime);
        Appointment appointment = build(new Appointment(), appDate, converted, appType, clientName,
                clientEmail, clientMsg);
        try {
            repository.add(appointment);
            return Response.ok(appointment).build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Something happened in the application "
                    + "and this apointment could not get saved. Please contact us "
                    + "to inform us of this issue.");
        }
    }

    @PUT
    @Path("{id}")
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response updateAppointment(@PathParam("id") int id,
            @FormParam("date") Date appDate, @FormParam("time") String appTime,
            @FormParam("type") String appType, @FormParam("clientName") String clientName,
            @FormParam("email") String clientEmail, @DefaultValue("")
            @FormParam("message") String clientMsg) {
        if (Integer.valueOf(id) == null) {
            throw new BadRequestException("The id of the "
                    + "appintment that you wish to modify hasn't been provided.");
        }
        //externalize the validation of all fields to concentrate on "positive"
        //scenario only
        validator(appDate, appType, clientName, clientEmail);
        Time converted = new SqlTimeConverter().fromString(appTime);
        Appointment appointment = repository.get(id);
        if (ofNullable(appointment).isPresent()) {
            repository.update(build(appointment, appDate, converted, appType, clientName,
                    clientEmail, clientMsg));
            //parameters for the return link
            Map<String, String> map = new HashMap<>();
            map.put("path", "appointments/" + Integer.toString(id));
            return Response.ok(uriGenerator.apply(appsUriBuilder, map)).build();

        }
        throw new BadRequestException("The appointment with " + id + " doesn't exist!");
    }

    @GET
    @Produces("application/json")
    public Response getAppointments(@DefaultValue("0") @QueryParam("offset") int offset,
            @DefaultValue("10") @QueryParam("size") int size) {
        //start by getting all appointments from EJB - the only available method
        List<Appointment> list;
        try {
            list = repository.getAll();
        } catch (Exception e) {
            throw new InternalServerErrorException("Something happened in the application "
                    + "- we couldn't retrieve the appointments. Please contact"
                    + " the support team.");
        }
        //externalize requested size validation
        int answerSize = sizeValidator(list.size(), offset, size);
        List<Appointment> current = list.stream().skip(offset).limit(answerSize)
                .collect(toList());
        JaxbAppointmentListWrapper response
                = new JaxbAppointmentListWrapperBuilder(answerSize, list.size(),
                        offset, current).compose();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String output;
        try {
            output = mapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AppointmentResourceService.class.getName()).log(Level.SEVERE, null, ex);
            output = "Code error serializing the appointments that you have requested";
        }
        return Response.ok(output).build();
    }

    @GET
    @Path("experimental")
    @Produces("application/json")
    public Response getExperimental(@FormParam("date") String appDate,
            @FormParam("time") String appTime, @FormParam("type") String appType,
            @FormParam("clientName") String clientName,
            @DefaultValue("0") @QueryParam("offset") int offset,
            @DefaultValue("10") @QueryParam("size") int size) {
        Time timeConverted = null;
        Date dateConverted = null;
        if (appTime != null && !appTime.equals("")) {
            timeConverted = new SqlTimeConverter().fromString(appTime);
        }
        if (appDate != null && !appDate.equals("")) {
            dateConverted = new SqlDateConverter().fromString(appDate);
        }
        //now we should be ready to call the triage class that will designate
        //the main query that will be called from repository as it is possible
        //that none of the Appointment params are specified the return type is Optional
        AppointmentsQueryCandidatesTriage triage = new AppointmentsQueryCandidatesTriage(appDate,
                appTime, appType, clientName);
        Optional<AppointmentsQueryCandidate> winner = triage.triage();
        //unverified map with all params as strings - may contain empty values
        Map<String, Object> stringMap = triage.allProps();
        //we will replace the strings with Date and Time objects for further calcs
        Map<String, Object> checkedParameters = new HashMap<>();
        //populate objectMap with non-empty strings only
        stringMap.forEach((k, v) -> {
            if (!v.equals("")) {
                checkedParameters.put(k, v);
            }
        });
        if (ofNullable(timeConverted).isPresent()) {
            checkedParameters.put("time", timeConverted);
        }
        if (ofNullable(dateConverted).isPresent()) {
            checkedParameters.put("date", dateConverted);
        }
        List<Appointment> initialSelection = new LinkedList<>();
        CompletableFuture<List<Appointment>> futureList = supplyAsync(
                () -> {
                    if (!winner.isPresent()) {
                        return repository.getAll();
                    } else {
                        return new QueryCommandControl().executeQuery(winner.get(), repository);
                    }
                }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        Set<String> unusedKeys = checkedParameters.keySet();
        if (winner.isPresent()) {
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        //if the list.size() ==0 return a corresponding Response
        //else do the forEach on checkedParameters to filter futureList.stream() 
        //with the remaining keys of checkedParameters
        //collect toList() and proceed with th rest of the code
        return null;
    }

    public int sizeValidator(int listSize, int requestOffset, int requestSize) {
        int answerSize;
        if (requestSize < 1) {
            throw new BadRequestException("You haven't requested any appointments");
        }
        if (listSize < 1) {
            throw new BadRequestException("There are no appointments that meet "
                    + "your request");
        }
        if ((listSize - requestOffset) < 1) {
            throw new BadRequestException("There are less appointments in the "
                    + "system than you have requested");
        }
        if ((listSize - requestOffset) < requestSize) {
            answerSize = listSize - requestOffset;
        } else {
            answerSize = requestSize;
        }
        return answerSize;
    }

    public boolean validator(Date appDate, String appType,
            String clientName, String clientEmail) {
        //validators->
        /*
         we will throw a bad request exception (400 http code) if the requested
         appointment is before 'today' or it is in 3 months from today
         */
        if (appType == null || appType.trim().equals("") || clientName == null
                || clientName.trim().equals("")
                || clientEmail == null || clientEmail.trim().equals("")) {
            throw new BadRequestException("The request has been rejected because "
                    + "you have not provided either type of appointment, client "
                    + "name or client email");
        }
        if (appDate.before(localToSqlDate(LocalDate.now()))
                || appDate.after(localToSqlDate(LocalDate.now().plusMonths(3)))) {
            throw new BadRequestException("You cannot take appointment on a "
                    + "past date or on a day which is 3+ months in the future.");
        }
        clientEmail = clientEmail.replaceAll("%40", "@");
        if (!clientEmail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            throw new BadRequestException("Please check the format of the "
                    + "email address that you"
                    + " are trying submit. Note that it is a mandatory field.");
        }
        return true;
    }

    private Appointment build(Appointment appointment, Date appDate, Time appTime, String appType,
            String clientName, String clientEmail, String clientMsg) {
        appointment.setDate(appDate);
        appointment.setTime(appTime);
        appointment.setClientName(clientName);
        appointment.setEmail(clientEmail);
        appointment.setType(appType);
        if (clientMsg != null && !clientMsg.trim().equals("")) {
            appointment.setMessage(clientMsg);
        }
        return appointment;
    }
}
