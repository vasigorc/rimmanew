/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.entities.Groups;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.AccessTimeout;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;

/**
 *
 * @author vgorcinschi
 */
@Local
@Lock(LockType.WRITE)
public interface GroupsRepository {
    
    public boolean addGroup(Groups group);
    
    public boolean updateGroups(Groups group);
    
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public Groups getByGroupName(String groupName);
    
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public List<Groups> getByCredential(Credential credential);
    
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public List<Groups> getAll();
}
