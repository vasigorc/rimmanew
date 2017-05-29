package com.vgorcinschi.rimmanew.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class InputValidatorsTest {
    
    public InputValidatorsTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void isOkPsswdTest(){
        assertTrue(InputValidators.isOkPsswd.test("hai1024!"));
    }
}
