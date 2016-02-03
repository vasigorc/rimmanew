/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services;

import com.vgorcinschi.rimmanew.annotations.JpaRepository;
import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlTimeConverter;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import java.net.URI;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Locale;
import static java.util.Optional.ofNullable;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

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
            //prepare the link to the updated entity
            UriBuilder builder = UriBuilder.fromPath("RimmaNew/rest/appointments/{id}");
            builder.scheme("http").host("{hostname}").port(8080);
            URI uri = builder.build("localhost", id);
            return Response.ok(uri).build();
            
        }
        throw new BadRequestException("The appointment with "+id+" doesn't exist!");
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
