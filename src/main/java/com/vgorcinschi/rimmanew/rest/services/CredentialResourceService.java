package com.vgorcinschi.rimmanew.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CredentialRepository;
import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.rest.services.helpers.GenericBaseJaxbListWrapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.JaxbCredentialListWrapperBuilder;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential.CredentialQueryCandidate;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential.CredentialQueryCandidatesTriage;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential.CredentialQueryCommandControl;
import com.vgorcinschi.rimmanew.util.ExecutorFactoryProvider;
import com.vgorcinschi.rimmanew.util.InputValidators;
import com.vgorcinschi.rimmanew.util.JavaSlangUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import static java.util.stream.Collectors.toList;
import javaslang.control.Try;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

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
                    String output = entityToJson(optCredential.get()).toString();
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

    @GET
    public Response allCredentials(@QueryParam("username") String username,
            @QueryParam("group") String group, 
            @QueryParam("firstName") String firstName, @QueryParam("lastName")
            String lastName, @QueryParam("email") String email,
            @DefaultValue("true") @QueryParam("isActive") String isActive,
            @DefaultValue("0") @QueryParam("offset") int offset,
            @DefaultValue("10") @QueryParam("size") int size){
        //if the isActive was provided - it should be correctly
        //converted to a boolean type
        String [] array = {isActive};
        if(!InputValidators.allStringsAreGood.apply(array) 
                && !InputValidators.validStringsAreTrueOrFalse.apply(array)){
            logger.error(isActive+" is not a valid Boolean value (\"true\" "
                    + "or \"false\") for the \"isActive\" parameter.");
            throw new BadRequestException("You provided an erroneous value "
                    + "for the 'isActive' parameter. You may "
                    + "only use 'true' or 'false'.",
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
        //safe to parse
        boolean booleanIsActive = Boolean.parseBoolean(isActive);
        //find the narrowest possible query
        CredentialQueryCandidatesTriage triage = 
                new CredentialQueryCandidatesTriage(username, group, booleanIsActive,
                firstName, lastName, email);
        //calculate the winner
        CompletableFuture<Optional<CredentialQueryCandidate>> futureWinner = 
                CompletableFuture.supplyAsync(()->{
            Optional<CredentialQueryCandidate> winner = triage.triage();
            return winner;
        }, ExecutorFactoryProvider.getSingletonExecutorOf30());
        //but eventually we need a list of appointments and not a type of query
        //so let us extend our asynchronous call
        CompletableFuture<List<Credential>> futureList = futureWinner.thenApply((Optional<CredentialQueryCandidate> winner) -> {
            if(winner.isPresent()){
                return new CredentialQueryCommandControl().executeQuery(winner.get(), repository);
            } else {
                return repository.getAll();
            }
        });
        //checkedParameters will contain non-empty values of correct type
        Map<String, Object> checkedParameters = new HashMap<>();
        triage.allProps().forEach((k,v) ->{
            if (!v.toString().equals("")) {
                checkedParameters.put(k, v);
            }
        });
        //set of keys of checked parameters
        Set<String> unusedKeys = new HashSet<>(checkedParameters.keySet());
        List<Credential> initialSelection = new LinkedList<>();
        //at this point we only need the first future
        Optional<CredentialQueryCandidate> winner = Optional.empty();
        Try<Optional<CredentialQueryCandidate>> tryCandidate = JavaSlangUtil.fromComplFuture(futureWinner);
        if(tryCandidate.isSuccess()){
            winner = tryCandidate.get();
        }else{
            logger.error("Failed to obtain CredentialQueryCandidate from "
                    + "a Future: "+tryCandidate.getCause());
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team.");
        }
        if(winner.isPresent()){
            //util.Set is mutable so this is fine
            unusedKeys.removeAll(winner.get().getParams().keySet());
        }
        /*
        if the list.size() ==0 return a corresponding Response,
        else do the forEach on checkedParameters to filter futureList.stream()
        with the remaining keys of checkedParameters
        */
        Try<List<Credential>> tryInitSelection = JavaSlangUtil.fromComplFuture(futureList);
        if(tryInitSelection.isSuccess()){
            initialSelection = tryInitSelection.get();
        } else {
            logger.error("Failed to obtain a List of Credentials from "
                    + "a Future: "+tryInitSelection.getCause());
            throw new InternalServerErrorException("It took the application "
                    + "too long to grab the results. Please contact the support team.");
        }
        String output;
        if(initialSelection.isEmpty()){
            GenericBaseJaxbListWrapper response =
                    new JaxbCredentialListWrapperBuilder(0,0,offset,initialSelection).compose();
            try {
                output = listWrapperToJson(response);
            } catch (Exception ex) {
                logger.error(ex.getMessage()+", location: "+ex.getMessage());
                throw new InternalServerErrorException("Code error serializing the appointments that you have requested.");
            }
            return Response.ok(output).build();
        }else{
            //do a foreach on the unused keys
            for(String k : unusedKeys){
                if (initialSelection.isEmpty()) {
                    break;
                }
                switch(k){
                    case "username": 
                        initialSelection = initialSelection.stream().filter(c -> c.getUsername().equals(k)).collect(toList());
                        break;
                    case "group":
                        initialSelection = initialSelection.stream().filter(c -> k.equalsIgnoreCase(c.getGroup().getGroupName())).collect(toList());
                        break;
                    case "isActive":
                        initialSelection = initialSelection.stream().filter(c -> (c.isBlocked() == booleanIsActive
                            && c.isSuspended() == booleanIsActive)).collect(toList());
                        break;
                    case "firstName":
                        initialSelection = initialSelection.stream().filter(c -> k.equalsIgnoreCase(c.getFirstname())).collect(toList());
                        break;
                    case "lastName":
                        initialSelection = initialSelection.stream().filter(c -> k.equalsIgnoreCase(c.getLastname())).collect(toList());
                        break;
                    case "email":
                        initialSelection = initialSelection.stream().filter(c -> k.equals(c.getEmailAddress())).collect(toList());
                        break;
                }       
            }
            int totalMatches = initialSelection.size();
            //figuring out how many can we actually return
            int answerSize = sizeValidator(totalMatches, offset, size);
            List<Credential> finalList = initialSelection.stream().skip(offset).limit(answerSize).collect(toList());
            GenericBaseJaxbListWrapper response
                    = new JaxbCredentialListWrapperBuilder(answerSize, totalMatches, offset, finalList, checkedParameters).compose();
            try {
                output = listWrapperToJson(response);
                return Response.ok(output).build();
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new InternalServerErrorException("Code error serializing the appointments that you have requested.");
            }
        }
    }
    
    @Override
    protected JsonObject entityToJson(Credential entity) {
        JsonObject value = factory.createObjectBuilder()
                .add("username", entity.getUsername())
                .add("group", entity.getGroup().getGroupName())
                .add("blocked", entity.isBlocked())
                .add("suspended", entity.isSuspended())
                .add("firstname", entity.getFirstname() != null ? entity.getFirstname() : "")
                .add("lastname", entity.getLastname() != null ? entity.getLastname() : "")
                .add("email", entity.getEmailAddress() !=null ? entity.getEmailAddress() : "")
                .add("metaInfo", factory.createObjectBuilder()
                .add("createdBy", entity.getCreatedBy())
                .add("createdOn", formatter.format(entity.getCreatedDate()))
                .add("modifiedBy", entity.getModifiedBy())
                .add("modifiedOn", formatter.format(entity.getModifiedDate()))
                        .build()).build();
        return value;
    }
 
    @Override
    protected Observable<Credential> rxEntityList(List<Credential> l){
        return Observable.defer(()->
                Observable.from(l)).subscribeOn(Schedulers.io());
    }
    
    private String listWrapperToJson(GenericBaseJaxbListWrapper response){
        JsonObjectBuilder listWrapper = Json.createObjectBuilder();
        JsonArrayBuilder current = Json.createArrayBuilder();
        //parallel json transformation with RxJava
        Subscriber<Credential> subscriber = new Subscriber<Credential>() {
            @Override
            public void onCompleted() {
                listWrapper.add("current", current);
            }

            @Override
            public void onError(Throwable thrwbl) {
                logger.error("Error serializaing one of the credentials for the "
                        + "final list wrapper: "+thrwbl.getMessage());
            }

            @Override
            public void onNext(Credential c) {
                current.add(entityToJson((Credential) c));
            }
        };
        rxEntityList((List<Credential>) response.getCurrent()).subscribe(subscriber);
        if(response.getFirst() != null){
            listWrapper.add("first", response.getFirst().toASCIIString());
        }
        if(response.getNext() != null){
            listWrapper.add("next", response.getNext().toASCIIString());
        }
        if(response.getLast() != null){
            listWrapper.add("last", response.getLast().toASCIIString());
        }
        if(response.getPrevious() != null){
            listWrapper.add("previous", response.getPrevious().toASCIIString());
        }
        if(response.getAll() != null){
            listWrapper.add("all", response.getAll().toASCIIString());
        }
        listWrapper.add("returnedSize", response.getReturnedSize());
        while (!subscriber.isUnsubscribed()) {//only return when the subscriber is done
        }
        //add root and return
        return factory.createObjectBuilder().add("credential", listWrapper).build().toString();
    }
}