/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import static javaslang.API.Case;
import static javaslang.API.Match;
import javaslang.Function3;
import static javaslang.Predicates.is;
import javax.validation.constraints.Size;
import rx.Observable;

/**
 * this will be a collection of static methods and lambdas to validate user
 * input
 *
 * @author vgorcinschi
 */
public class InputValidators {

    public static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /*
        ^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=\S+$)          # no whitespace allowed in the entire string
        .{8,}             # anything, at least eight places though
        $                 # end-of-string
    +++++++consider also adding this when needed+++++++++
        (?=.*[A-Z])       # an upper case letter must occur at least once
     */
    public static final String PASSWORD_RULES
            = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";

    public static Function<String[], Boolean> allStringsAreGood = (java.lang.String[] array) -> {
        if (Arrays.asList(array).isEmpty()) {
            return false;
        }
        return Arrays.asList(array).stream().allMatch((str) -> str != null && !str.equals(""));
    };

    public static Function<String[], Boolean> validStringsAreTrueOrFalse = (java.lang.String[] array) -> {
        List<String> list = Arrays.asList(array);
        return list.stream().allMatch((str) -> {
            return str.trim().equalsIgnoreCase("false")
                    || str.trim().equalsIgnoreCase("true");
        });
    };

    public static Function<String, Boolean> stringNotNullNorEmpty = (str) -> {
        return str != null && !str.equals("");
    };

    public static Function<String, Boolean> stringIsValidDate = (String date) -> {
        if (date == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            java.util.Date utilDate = sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    public static Predicate<String> isValidEmail = (String candidate) -> {
        return stringNotNullNorEmpty.apply(candidate)
                && candidate.matches(EMAIL_PATTERN);
    };

    public static Predicate<String> isOkPsswd = (String candidate) -> {
        return candidate.matches(PASSWORD_RULES);
    };

    public static Function3<Class<?>, String, String, Boolean> validateAnnotatedField
            = (clazz, fieldString, literal) -> {
                try {
                    Field field = clazz.getField(fieldString);
                    Observable<Annotation> fieldAnnotations = Observable.from(field.getAnnotations());
                    fieldAnnotations.subscribe(annotation -> {//onNext
                        Match(annotation.annotationType()).of(
                                Case(is(Size.class), () -> {
                                    Size sizeAnnotation = field.getAnnotation(Size.class);
                                    return true;
                                })
                        );
                    });
                } catch (NoSuchFieldException e) {
                }
                return Boolean.TRUE;
            };
}
