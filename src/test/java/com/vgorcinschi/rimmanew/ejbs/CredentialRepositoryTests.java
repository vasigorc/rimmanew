/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.entities.Groups;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
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
    @Ignore
    public void createCredentialTest() {
        //retrieve the group
        Groups admin = groupRepo.getByGroupName("admin");
        Credential sampleUser = new Credential("user_creator", "sample_user", "abc123");
        admin.addCredential(sampleUser);
        assertTrue(groupRepo.updateGroups(admin));
    }

    @Test
    @Ignore
    public void savingAnotherCredential() {
        Groups admin = groupRepo.getByGroupName("admin");
        Credential anoterUser = new Credential("user_creator", "sample_user_two", "abc123");
        admin.addCredential(anoterUser);
        assertTrue(groupRepo.updateGroups(admin));
    }

    @Test
    public void retrieveACredentialByUsername() {
        assertNotNull(credentialRepo.getByUsername("sample_user_two"));
    }

    @Test
    public void updateACredential() {
        Credential c = credentialRepo.getByUsername("sample_user_two");
        c.setFirstname("John");
        c.setLastname("Smith");
        assertTrue(credentialRepo.updateCredential(c));
    }

    @Test
    public void getAllAdmins() {
        assertTrue(credentialRepo.getByGroups("admin").size() > 1);
    }

    @Test
    public void getAllCredentials() {
        assertTrue(!credentialRepo.getAll().isEmpty());
    }
    
    @Test
    public void getActiveCredentials(){
        assertTrue(credentialRepo.getActive().size() > 1);
    }
}
