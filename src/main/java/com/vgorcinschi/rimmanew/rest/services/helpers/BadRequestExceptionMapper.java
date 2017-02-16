/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author vgorcinschi
 */
@Provider
public class BadRequestExceptionMapper implements ExceptionMapper < BadRequestException > {
  @Override
  public Response toResponse(BadRequestException exception) {
    return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(exception.getMessage()).build();
  }
}
