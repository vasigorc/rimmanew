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
    private final Map<String, Object> allProps;
    
    public CredentialQueryCandidatesTriage(String username, String group,
            Boolean isActive) {
        Comparator<CredentialQueryCandidate> byPriority
                = (c1, c2) -> (Integer) (c2.getPriority() -c1.getPriority());
        pq = new PriorityQueue<>(byPriority);
        allProps = new HashMap();
        allProps.put("username", ofNullable(username).orElse(""));
        allProps.put("group", ofNullable(group).orElse(""));
        allProps.put("isActive", ofNullable(isActive));
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
        return allProps;
    }
    
    public void getByUsername(){
        if(!allProps.get("username").equals("")){
            Map<String, Object> map = new HashMap<>();
            map.put("username", allProps.get("username"));
            pq.offer(new GetByUsernameQuery(map));
        }
    }
    
    public void getByGroup(){
        if(!allProps.get("group").equals("")){
            Map<String, Object> map = new HashMap<>();
            map.put("group", allProps.get("group"));
            pq.offer(new GetByGroupQuery(map));
        }
    }
    
    public void getByIsActive(){
        if(allProps.get("isActive") != null){
            Map<String, Object> map = new HashMap<>();
            map.put("isActive", allProps.get("isActive"));
            pq.offer(new GetByIsActiveQuery(map));
        }
    }
}