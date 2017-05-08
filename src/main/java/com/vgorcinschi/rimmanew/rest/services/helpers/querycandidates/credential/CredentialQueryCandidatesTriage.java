package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential;

import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QueryCandidateTriage;
import com.vgorcinschi.rimmanew.util.InputValidators;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.PriorityQueue;

/**
 *
 * @author vgorcinschi
 */
public class CredentialQueryCandidatesTriage implements QueryCandidateTriage{

    private final PriorityQueue<CredentialQueryCandidate> pq;
    private final Map<String, Object> mainProps;
    private final Map<String, Object> auxProps;
    
    public CredentialQueryCandidatesTriage(String username, String group,
            Boolean isActive, String firstName, String lastName, String email) {
        Comparator<CredentialQueryCandidate> byPriority
                = (c1, c2) -> (Integer) (c2.getPriority() -c1.getPriority());
        pq = new PriorityQueue<>(byPriority);
        mainProps = new HashMap();
        auxProps = new HashMap<>();
        mainProps.put("username", ofNullable(username).orElse(""));
        mainProps.put("group", ofNullable(group).orElse(""));
        mainProps.put("isActive", ofNullable(isActive));
        auxProps.put("firstName", ofNullable(firstName).orElse(""));
        auxProps.put("lastName", ofNullable(lastName).orElse(""));
        auxProps.put("email", ofNullable(email).orElse(""));
    }
    
    @Override
    public Optional<CredentialQueryCandidate> triage() {
        getByGroup();
        getByIsActive();
        getByUsername();
        //by this point the PriorityQueue is only populated with non-null values
        return Optional.ofNullable(pq.peek());
    }

    @Override
    public Map<String, Object> allProps() {
        return mainProps;
    }
    
    public void getByUsername(){
        if(!mainProps.get("username").equals("")){
            Map<String, Object> map = new HashMap<>();
            map.put("username", mainProps.get("username"));
            pq.offer(new GetByUsernameQuery(map));
        }
    }
    
    public void getByGroup(){
        if(!mainProps.get("group").equals("")){
            Map<String, Object> map = new HashMap<>();
            map.put("group", mainProps.get("group"));
            pq.offer(new GetByGroupQuery(map));
        }
    }
    
    public void getByIsActive(){
        if(mainProps.get("isActive") != null){
            Map<String, Object> map = new HashMap<>();
            map.put("isActive", mainProps.get("isActive"));
            pq.offer(new GetByIsActiveQuery(map));
        }
    }
    
    public void remainderOfTheParams(Map<String, Object> map){
        
        auxProps.forEach((k, v) ->{
            if(InputValidators.stringNotNullNorEmpty.apply(k)){
                map.put(k, v);
            }
        });
    }
}