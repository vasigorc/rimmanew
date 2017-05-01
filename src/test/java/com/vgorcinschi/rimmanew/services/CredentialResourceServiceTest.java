package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CredentialRepository;
import com.vgorcinschi.rimmanew.ejbs.JpaCredentialRepository;
import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.helpers.InstantConverter;
import com.vgorcinschi.rimmanew.rest.services.CredentialResourceService;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author vgorcinschi
 */
@RunWith(Arquillian.class)
public class CredentialResourceServiceTest {
    
    @Deployment
    @RunAsClient
    public static WebArchive createDeployment(){
        return ShrinkWrap.create(WebArchive.class, "credentialRestService.war")
                .addClasses(CredentialRepository.class,
                        JpaCredentialRepository.class,
                        CredentialResourceService.class,
                        FakeCredentialApp.class)
                .addPackage(Credential.class.getPackage())
                .addPackage(InstantConverter.class.getPackage())
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Inject
    @Production
    private CredentialRepository credentialRepository;
    
    public CredentialResourceServiceTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void repositoryNotNull(){
        assertNotNull(credentialRepository);
    }
    
    @Test
    public void getUserWithoutRestCall(){
        Credential byUsername = credentialRepository.getByUsername("admin");
        System.out.println(byUsername);
        assertNotNull(byUsername);
    }
    
    @Test
    public void testTest(){
        System.out.println(credentialRepository.getAll());
    }
}
