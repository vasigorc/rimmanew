package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CredentialRepository;
import com.vgorcinschi.rimmanew.ejbs.GroupsRepository;
import com.vgorcinschi.rimmanew.ejbs.JpaCredentialRepository;
import com.vgorcinschi.rimmanew.ejbs.JpaGroupsRepository;
import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.helpers.InstantConverter;
import com.vgorcinschi.rimmanew.rest.services.CredentialResourceService;
import com.vgorcinschi.rimmanew.util.SecurityPrompt;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.ArquillianProxyException;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author vgorcinschi
 */
@RunWith(Arquillian.class)
public class CredentialResourceServiceTest {

    @ArquillianResource
    private URL deploymentURL;
    private final JsonBuilderFactory FACTORY;
    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    private final String TEST_ADMIN = "testAdmin";

    @Deployment
    @RunAsClient
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "credentialRestService.war")
                .addClasses(CredentialRepository.class,
                        GroupsRepository.class,
                        JpaGroupsRepository.class,
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
    
    @Inject
    @Production
    private GroupsRepository groupsRepository;

    public CredentialResourceServiceTest() {
        Map<String, Object> configs = new HashMap<>(1);
        configs.put(JsonGenerator.PRETTY_PRINTING, true);
        this.FACTORY = Json.createBuilderFactory(configs);
    }

    public void setUp() {
        Credential testAdmin = new Credential(TEST_ADMIN, TEST_ADMIN);
        testAdmin.setEmailAddress("testAdmin@gmail.com");
        testAdmin.setBlocked(false);
        testAdmin.setSuspended(false); 
        testAdmin.setPasswd(SecurityPrompt.toBytePassword(TEST_ADMIN, testAdmin.getSalt()));
        testAdmin.setGroup(groupsRepository.getByGroupName("admin"));

        boolean testAdminCreated = credentialRepository.createCredential(testAdmin);
        LOGGER.info(String.format("Created user %s: %b", TEST_ADMIN, testAdminCreated));
    }

    public void tearDown() {
        credentialRepository.delete(TEST_ADMIN);
    }

    @Test
    public void repositoryNotNull() {
        assertNotNull(credentialRepository);
    }

    @Test
    public void getUserWithoutRestCall() {
        setUp();
        Credential byUsername = credentialRepository.getByUsername(TEST_ADMIN);
        assertNotNull(byUsername);
        tearDown();
    }

    @Test
    public void getByUsername(
            @ArquillianResteasyResource CredentialResourceService crs) {
        setUp();
        final Response response = crs.getCredential(TEST_ADMIN);
        System.out.println(response.getEntity());
        assertTrue(response.getEntity().toString().contains(TEST_ADMIN));
        tearDown();
    }

    @Test
    public void getAllUsers(@ArquillianResteasyResource CredentialResourceService crs) {
        final Response response = crs.allCredentials(null, null, null, null, null, "true", 0, 10);
        assertTrue(response.getStatus() == 200);
        System.out.println(response.getEntity().toString());
    }

    @Test(expected = ArquillianProxyException.class)
    public void isActiveInvalidValue(@ArquillianResteasyResource CredentialResourceService crs) {
        final Response response = crs.allCredentials(null, null, null, null, null, "somethingElse", 0, 10);
        assertTrue(response.getStatus() == 200);
    }

    @Test
    public void updateCredentialTest(@ArquillianResteasyResource CredentialResourceService crs) {
        setUp();
        JsonObjectBuilder objectBuilder = FACTORY.createObjectBuilder()
                .add("username", TEST_ADMIN)
                .add("password", "hai1024!")
                .add("group", "admin")
                .add("blocked", false)
                .add("suspended", false)
                .add("firstName", "Elena")
                .add("lastName", "Gorcinschi")
                .add("emailAddress", "elenatodorasco@gmail.com")
                .add("updatedBy", "su-user");
        JsonObject value = objectBuilder.build();
        Response response = crs.updateCredential(TEST_ADMIN, streamFromJson(value));
        assertTrue(response.getStatus() == 200);
        tearDown();
    }

    @Test(expected = ArquillianProxyException.class)
    public void inproperCredentialCandidateSentTest(@ArquillianResteasyResource CredentialResourceService crs) {
        JsonObject value = FACTORY.createObjectBuilder()
                .add("username", TEST_ADMIN)
                .add("password", "hai1024!")
                .add("group", "admin")
                .add("blocked", "stringThatShouldn'tBeHere")//error is here!
                .add("suspended", false)
                .add("firstName", "Elena")
                .add("lastName", "Gorcinschi")
                .add("emailAddress", "elenatodorasco@gmail.com")
                .add("updatedBy", "su-user").build();
        final Response response = crs.updateCredential(TEST_ADMIN, streamFromJson(value));
        assertTrue(response.getStatus() == 200);
    }
    
    @Test
    public void createCredentialTest(@ArquillianResteasyResource CredentialResourceService crs){
        JsonObject value = FACTORY.createObjectBuilder()
                .add("username", "tugudu")
                .add("password", "tugudu2018")
                .add("group", "admin")
                .add("blocked", false)
                .add("firstName", "Elena")
                .add("lastName", "Gorcinschi")
                .add("emailAddress", "tugudu@gmail.com")
                .add("updatedBy", "su-user").build();
        final Response response = crs.createCredential(streamFromJson(value));
        assertTrue(response.getStatus() == 200);
        credentialRepository.delete("tugudu");
    }
    
    private InputStream streamFromJson(JsonObject value){
        return new ByteArrayInputStream(value.toString().getBytes());
    }
}
