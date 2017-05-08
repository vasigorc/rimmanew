package com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential;

import com.vgorcinschi.rimmanew.ejbs.CredentialRepository;
import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QueryCommand;
import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.QueryCommandControl;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author vgorcinschi
 */
public class CredentialQueryCommandControl implements QueryCommandControl<CredentialQueryCandidate, 
        Credential, CredentialRepository>{
    
    private final Map<CredentialQuerySignature, QueryCommand<CredentialQueryCandidate, Credential, CredentialRepository>> queryCommands;

    public CredentialQueryCommandControl() {
        this.queryCommands = new HashMap<>();
        /*
        lambda impls of the Credential specific QuerryCommand interface
        */
        this.queryCommands.put(CredentialQuerySignature.getByGroup, 
                (CredentialQueryCandidate cand, CredentialRepository repo) -> {
                    return repo.getByGroups((String) cand.getParams().get("group"));
                });
        this.queryCommands.put(CredentialQuerySignature.getByIsActive, 
                (CredentialQueryCandidate cand, CredentialRepository repo) -> {
                    boolean isActive = (boolean) cand.getParams().get("isActive");
                    List<Credential> list;
                    if(isActive){
                        list = repo.getActive();
                    }else{
                        list = repo.getAll().stream().filter(c -> c.isBlocked() || c.isBlocked()).collect(toList());
                    }
                    return list;
                });
        this.queryCommands.put(CredentialQuerySignature.getByUsername, 
                (CredentialQueryCandidate cand, CredentialRepository repo) -> {
                    List<Credential> list = new LinkedList<>();
                    Credential c = repo.getByUsername((String) cand.getParams().get("username"));
                    list.add(c);
                    return list;
                });
    }

    @Override
    public List<Credential> executeQuery(CredentialQueryCandidate cand, CredentialRepository repo) {
        return queryCommands.get(cand.getQuerySignature()).execute(cand, repo);
    }
}