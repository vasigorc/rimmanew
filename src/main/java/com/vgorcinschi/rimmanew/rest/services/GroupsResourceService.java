package com.vgorcinschi.rimmanew.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.GroupsRepository;
import com.vgorcinschi.rimmanew.entities.Groups;
import com.vgorcinschi.rimmanew.rest.services.helpers.GenericBaseJaxbListWrapper;
import com.vgorcinschi.rimmanew.util.InputValidators;
import java.util.List;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import javaslang.control.Try;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author vgorcinschi
 */
@Path("/groups")
@Produces("application/json")
public class GroupsResourceService extends RimmaRestService<Groups>{
    
    @Inject
    @Production
    private GroupsRepository groupsRepository;

    public void setGroupsRepository(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }
    
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
                    logger.error("Serialization error: "+e.getMessage()+
                            "\n"+e.getClass().getCanonicalName());
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
    
    @GET
    @Produces("application/json")
    public Response getGroups(@DefaultValue("0") @QueryParam("offset") int offset,
            @DefaultValue("10") @QueryParam("size") int size){
        Optional<List<Groups>> optGroups = ofNullable(groupsRepository.getAll());
        if(optGroups.isPresent() && !optGroups.get().isEmpty()){
            List<Groups> groups = optGroups.get();
            int totalGroups = groups.size();
            //how many are we actually returning + size validation
            int answerSize = sizeValidator(totalGroups, offset, size);
            //skip offset + limit to answerSize
            List<Groups> finalList = groups.stream().skip(offset)
                    .limit(answerSize).collect(toList());
            Try<GenericBaseJaxbListWrapper> tryResponse = 
                    listWrapperFactory("specialday", answerSize, totalGroups, offset, finalList);
            if(tryResponse.isSuccess()){
                try {
                    String output = getMapper().writeValueAsString(tryResponse.get());
                    return Response.ok(output).build();
                } catch (JsonProcessingException ex) {
                    logger.error("Serialization error: "+ex.getMessage()+
                            "\n"+ex.getClass().getCanonicalName());
                    throw new InternalServerErrorException("Server error "
                            + " serializing requested groups");
                }
            }else{
               logger.error("Serialization error: "+tryResponse.getCause().toString()+
                            "\n"+tryResponse.getCause().getClass().getSimpleName());
                    throw new InternalServerErrorException("Server error "
                            + " serializing requested groups"); 
            }            
        } else{
            throw new BadRequestException("Currently there are no "
                    + " currently stored groups.", 
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
    }
}
