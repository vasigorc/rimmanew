/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * this will be a collection of static methods and lambdas to validate user
 * input
 *
 * @author vgorcinschi
 */
public class InputValidators {

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
}
