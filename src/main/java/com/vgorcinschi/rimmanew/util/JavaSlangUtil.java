package com.vgorcinschi.rimmanew.util;

import java.util.Arrays;
import javaslang.control.Validation;

/**
 * utility methods based on the open-source
 * JavaSlang library
 * @author vgorcinschi
 */
public class JavaSlangUtil {
    
    public static <T> Validation<Exception, T[]> arrayNonEmpty(T[] array){
        /**
         * the test is that the passed array contains at least one non null
         * value.
         */
        return (array != null && array.length > 0 
                && Arrays.stream(array)
                        .anyMatch(elem -> elem !=null && !"".equals(elem.toString()))) ?
                Validation.valid(array)
                :Validation.invalid(new IllegalArgumentException("Array "+Arrays.deepToString(array)+" is effectively empty."));
    }
}