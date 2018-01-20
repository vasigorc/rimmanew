package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.ejbs.GroupsRepository;
import com.vgorcinschi.rimmanew.ejbs.OCGroupsRepository;
import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.entities.Groups;
import com.vgorcinschi.rimmanew.rest.services.CredentialResourceService;
import com.vgorcinschi.rimmanew.rest.services.helpers.CredentialCandidate;
import java.util.List;
import javaslang.Tuple2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import rx.Observable;

/**
 *
 * @author vgorcinschi
 */
public class InputValidatorsTest {
    
    private final Logger log = LogManager.getLogger(getClass());
    private CredentialResourceService crs;
    private final GroupsRepository repository;
    private final Groups ADMIN_GROUP;

    public InputValidatorsTest() {
        repository = new OCGroupsRepository();
        ADMIN_GROUP = repository.getByGroupName("admin");
    }

    @Before
    public void setUp() {
        crs = new CredentialResourceService();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void isOkPsswdTest() {
        assertTrue(InputValidators.isOkPsswd.test("hai1024!"));
    }

    @Test
    public void invalidCredentialFields() {
        Class<?> cc = Credential.class;
        Observable<String> stringSizeErrors = InputValidators.validateAnnotatedField(cc, "firstname", "");
        List<String> sringSizeResults = stringSizeErrors.toList().toBlocking().single();
        assertThat(sringSizeResults, is(not(empty())));
        
        Observable<String> patternErrors = InputValidators.validateAnnotatedField(cc, "emailAddress", "@ca");
        List<String> patternResults = patternErrors.toList().toBlocking().single();
        assertThat(patternResults, hasSize(2));
    }
    
    @Test
    public void invalidCredentialCandidate(){
        CredentialCandidate candidate = new CredentialCandidate();
        candidate.setUsername("qw");//should be at least four chars -1
        candidate.setEmailAddress("@ca");//doesn't match the pattern AND is too short -3
        candidate.setGroup(null);//should not be null -4
        candidate.setLastName("Gorcinschi");//is ok
        candidate.setFirstName("");//shouldn't be empty -5 
        candidate.setPassword("abc123");//password too weak -6
        Tuple2<Boolean, List<String>> validator = crs.validator(candidate);
        List<String> errors = validator._2();
        errors.stream().forEach(s->log.info(s+"\n"));
        assertThat(errors, hasSize(6));
    }

    @Test
    public void validCredentialCandidate(){
        CredentialCandidate candidate = new CredentialCandidate();
        candidate.setUsername("vgorcinschi");
        candidate.setEmailAddress("vasigorc@yahoo.ca");
        candidate.setGroup(ADMIN_GROUP.getGroupName());
        candidate.setLastName("Gorcinschi");
        candidate.setFirstName("Vasile");
        candidate.setPassword("5oph!st!cat4D");
        Tuple2<Boolean, List<String>> validator = crs.validator(candidate);
        List<String> errors = validator._2();
        assertThat(errors, is(empty()));
    }
}
