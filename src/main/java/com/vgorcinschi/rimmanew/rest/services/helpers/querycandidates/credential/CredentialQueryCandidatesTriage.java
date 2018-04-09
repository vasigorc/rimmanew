package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential;

import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QueryCandidateTriage;
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
    
    public CredentialQueryCandidatesTriage(String username, String group,
            Boolean isActive, String firstName, String lastName, String email) {
        Comparator<CredentialQueryCandidate> byPriority
                = (c1, c2) -> (Integer) (c2.getPriority() -c1.getPriority());
        pq = new PriorityQueue<>(byPriority);
        mainProps = new HashMap();
        mainProps.put("username", ofNullable(username).orElse(""));
        mainProps.put("group", ofNullable(group).orElse(""));
        mainProps.put("isActive", isActive);
        mainProps.put("firstName", ofNullable(firstName).orElse(""));
        mainProps.put("lastName", ofNullable(lastName).orElse(""));
        mainProps.put("email", ofNullable(email).orElse(""));
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
}