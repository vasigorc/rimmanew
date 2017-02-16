/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author vgorcinschi
 */
@Provider
public class InternalServerErrorExceptionMapper 
implements ExceptionMapper <InternalServerErrorException>{

    @Override
    public Response toResponse(InternalServerErrorException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(exception.getMessage()).build();
    }
    
}
