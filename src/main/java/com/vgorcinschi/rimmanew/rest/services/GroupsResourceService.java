package com.vgorcinschi.rimmanew.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.GroupsRepository;
import com.vgorcinschi.rimmanew.entities.Groups;
import com.vgorcinschi.rimmanew.util.InputValidators;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author vgorcinschi
 */
@Path("/groups")
@Produces("application/json")
public class GroupsResourceService {
    
    @Inject
    @Production
    private GroupsRepository groupsRepository;
    
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(this.getClass());
    
    @GET
    @Path("{group}")
    public Response getGroup(@PathParam("group") String group){
        if(InputValidators.stringNotNullNorEmpty.apply(group)){
            //proceed with making call to the repo
            Optional<Groups> optGroup = ofNullable(groupsRepository.getByGroupName(group));
            if(optGroup.isPresent()){//ultimately success scenario
                try {
                    String output = getMapper().writeValueAsString(optGroup.get());
                    return Response.ok(output).build();
                } catch (JsonProcessingException e) {
                    logger.error("Serialization error: "+e.getMessage());
                    throw new InternalServerErrorException("Server error "
                            + " serializing the requested group \""+group+"\"");
                }
            } else{
                throw new NotFoundException("Group " + group + " could not be found");
            }
        }else{//empty space after the slash
             throw new BadRequestException("You haven't provided a group parameter",
                        Response.status(Response.Status.BAD_REQUEST).build());
        }
    }
    
    public ObjectMapper getMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return mapper;
    }
}
