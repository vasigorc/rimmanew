package com.vgorcinschi.rimmanew.util;

import static com.vgorcinschi.rimmanew.util.JavaSlangUtil.arrayNonEmpty;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class JavaSlangUtilTests {

    @Test
    public void nonEmptyArrayWithEmptyString(){
        String[]a = {""};
        assertTrue(arrayNonEmpty(a).isInvalid());
    }
    
    @Test
    public void nonEmptyArrayTest() {
        String[]a = {"one"};
        assertTrue(arrayNonEmpty(a).isValid());
    }
    
    @Test
    public void emptyArrayTest(){
        String[]b = new String[1];
        assertTrue(arrayNonEmpty(b).isInvalid());
    }
}
