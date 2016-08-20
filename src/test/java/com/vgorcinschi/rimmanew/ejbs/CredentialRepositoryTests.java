/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.entities.Groups;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class CredentialRepositoryTests {
    
    private final OCGroupsRepository groupRepo;
    private final OCCredentialRepository credentialRepo;
    
    public CredentialRepositoryTests() {
        groupRepo = new OCGroupsRepository();
        credentialRepo = new OCCredentialRepository();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void createCredentialTest(){
        //retrieve the group
        Groups admin = groupRepo.getByGroupName("admin");
        Credential rimma = new Credential("user_creator", "sample_user", "abc123");
        admin.addCredential(rimma);
        assertTrue(groupRepo.updateGroups(admin));
    }
}
