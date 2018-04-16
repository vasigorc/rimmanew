/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Credential;
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
public interface CredentialRepository {
    
    public boolean createCredential(Credential credential);
    
    public boolean updateCredential(Credential credential);
    
    public boolean delete(String username);
    
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public Credential getByUsername(String username);
    
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public List<Credential> getByGroups(String group);
    
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public List<Credential> getActive();
    
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public List<Credential> getAll();
    
    @AccessTimeout(unit = TimeUnit.SECONDS, value = 15)
    public boolean emailIsAssigned(String... args);
}
