/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import javaslang.Function3;
import javaslang.Function2;
import static javaslang.Predicates.instanceOf;
import javaslang.control.Try;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    public static <T> Observable<String> validateAnnotatedField(Class<?> clazz,
            String fieldString, final T value) {
        Try<Field> tryField = Try.of(() -> clazz.getDeclaredField(fieldString));
        if (tryField.isFailure()) {
            return Observable.empty();
        }
        final Field field = tryField.get();
        if (value == null) {
            return Observable.just(String.format("Field %s was undefined, but it needs to be provided", field.getName()));
        }
        Observable<Annotation> fieldAnnotations = Observable.from(field.getAnnotations());
        return fieldAnnotations.concatMapIterable(annotation -> {
            return Match(value).of(
                    Case($(instanceOf(String.class)), validateStringAnnotation.apply(field, (String) value, annotation)),
                    Case($(), () -> new ArrayList())
            );
        });
    }

    private static final Function2<Field, String, List<String>> sizeMsgs
            = (Field field, String input) -> {
                List<String> errors = new ArrayList();

                Size size = field.getAnnotation(Size.class);
                if (size.min() != 0 && size.min() > input.length()) {
                    errors.add(String.format("Field %s is too short, minimum "
                            + "required length is %d character(s)", field.getName(), size.min()));
                }
                if (size.max() != 0 && size.max() < input.length()) {
                    errors.add(String.format("Field %s is too long, maximum "
                            + "length is %d characters", field.getName(), size.max()));
                }
                return errors;
            };

    private static final Function2<Field, String, List<String>> regexMsgs
            = (Field field, String input) -> {
                List<String> errors = new ArrayList();
                Pattern pattern = field.getAnnotation(Pattern.class);
                if (pattern.regexp() != null && !input.matches(pattern.regexp())) {
                    errors.add(String.format("Input %s doesn't match "
                            + "the required pattern \"%s\" for field %s",
                            input, pattern.regexp(), field.getName()));
                }
                return errors;
            };
    private static final Function2<Field, String, List<String>> notNullMsgs
            = (Field field, String input) -> {
                List<String> errors = new ArrayList();
                if (!stringNotNullNorEmpty.apply(input)) {
                    errors.add(String.format("Field % cannot be null"
                            + " nor an empty String.", field.getName()));
                }
                return errors;
            };

    private static final Function3<Field, String, Annotation, List<String>> validateStringAnnotation
            = (Field field, String input, Annotation annotation) -> {
                return Match(annotation).of(
                        Case($(instanceOf(Size.class)), () -> sizeMsgs.apply(field, input)),
                        Case($(instanceOf(Pattern.class)), () -> regexMsgs.apply(field, input)),
                        Case($(instanceOf(NotNull.class)), () -> notNullMsgs.apply(field, input)),
                        //default case
                        Case($(), () -> new ArrayList()));
            };
}
