package com.vgorcinschi.rimmanew.rest.services;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CredentialRepository;
import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.util.InputValidators;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vgorcinschi
 */
@Path("/credential")
public class CredentialResourceService extends RimmaRestService<Credential>{
    
    @Inject
    @Production
    private CredentialRepository repository;
    
    private final Logger logger = LogManager.getLogger(this.getClass());
    
    @GET
    @Path("{username}")
    public Response getCredential(@PathParam("username") String username){
        if(InputValidators.stringNotNullNorEmpty.apply(username)){
            //proceed with making call to the repo
            Optional<Credential> optCredential = ofNullable(repository.getByUsername(username));
            if(optCredential.isPresent()){
                try {
                    String output = toJSON(optCredential.get());
                    return Response.ok(output).build();
                } catch (Exception e) {
                    logger.error("Serialization error: "+e.getMessage()+
                            "\n"+e.getClass().getCanonicalName());
                    throw new InternalServerErrorException("Server error "
                            + " serializing the requested user \""+username+"\"");
                }
            } else {
                throw new NotFoundException("Username "+username+" could not be found.");
            }
        } else {//empty space after the slash
            throw new BadRequestException("You haven't provided the username.",
            Response.status(Response.Status.BAD_REQUEST).build());
        }
    }

    @Override
    protected String toJSON(Credential entity) {
        JsonObject value = factory.createObjectBuilder()
                .add("credential", factory.createObjectBuilder()
                        .add("username", entity.getUsername())
                        .add("group", entity.getGroup().getGroupName())
                        .add("blocked", entity.isBlocked())
                        .add("suspended", entity.isSuspended())
                        .add("firstname", entity.getFirstname())
                        .add("lastname", entity.getLastname())
                        .add("email", entity.getEmailAddress())
                        .add("metaInfo", factory.createObjectBuilder()
                        .add("createdBy", entity.getCreatedBy())
                        .add("createdOn", formatter.format(entity.getCreatedDate()))
                        .add("modifiedBy", entity.getModifiedBy())
                        .add("modifiedOn", formatter.format(entity.getModifiedDate()))
                                .build()).build()).build();
        return value.toString();
    }
}
