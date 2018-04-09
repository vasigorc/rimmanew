package com.vgorcinschi.rimmanew.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 *
 * @author vgorcinschi
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, PARAMETER, TYPE, FIELD})
public @interface OCRepository {
    
}
