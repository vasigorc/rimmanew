/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.entities.Groups;
import static com.vgorcinschi.rimmanew.util.SecurityPrompt.pbkdf2;
import java.util.Arrays;
import org.junit.After;
import static org.junit.Assert.assertFalse;
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
        Credential sampleUser = new Credential("user_creator", "sample_user_nr3");
        sampleUser.setPasswd(pbkdf2("dnf2", sampleUser.getSalt(),
                120000, 512));
        sampleUser.setGroup(admin);
        assertTrue(credentialRepo.createCredential(sampleUser));
    }
    
    @Test
    @Ignore
    public void createSuCredentialTest(){
        //retrieve the group
        Groups su = groupRepo.getByGroupName("su");
        Credential suUser = new Credential("Vasile", "su-user");
        suUser.setPasswd(pbkdf2("qwerty", suUser.getSalt(), 120000, 512));
        suUser.setGroup(su);
        assertTrue(credentialRepo.createCredential(suUser));
    }

    @Test
    @Ignore
    public void savingAnotherCredential() {
        Groups admin = groupRepo.getByGroupName("admin");
        Credential anoterUser = new Credential("user_creator", "sample_user_two");
        anoterUser.setPasswd(pbkdf2("abc123", anoterUser.getSalt(), 120000, 512));
        admin.addCredential(anoterUser);
        assertTrue(groupRepo.updateGroups(admin));
    }

    @Test
    public void canComparePasswords() {
        //retrieve an user
        Credential c = credentialRepo.getByUsername("sample_user_two");
        //get the encoded password
        byte[] storedPassword = c.getPasswd();
        //reproduce he encoded password with the same input string and salt
        byte[] testPassword = pbkdf2("abc123", c.getSalt(), 120000, 512);
        assertTrue(Arrays.equals(storedPassword, testPassword));
        System.out.println("\ncanComparePasswords\nstoredPassword: "
                + "" + Arrays.toString(storedPassword) + "\ntestPassword: "
                + Arrays.toString(testPassword));
    }
    
    @Test
    public void wrongPasswordTest(){
        //retrieve an user
        Credential c = credentialRepo.getByUsername("sample_user_two");
        //get the encoded password
        byte[] storedPassword = c.getPasswd();
        //same salt BUT the password is different in one letter only
        byte[] testPassword = pbkdf2("aBc123", c.getSalt(), 120000, 512);
        assertFalse(Arrays.equals(storedPassword, testPassword));
        System.out.println("\nwrongPasswordTest\nstoredPassword: "
                + "" + Arrays.toString(storedPassword) + "\ntestPassword: "
                + Arrays.toString(testPassword));
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
    public void getActiveCredentials() {
        assertTrue(credentialRepo.getActive().size() > 1);
    }
}