package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.entities.Credential;
import java.util.List;
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

    public InputValidatorsTest() {
    }

    @Before
    public void setUp() {
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
}
