/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author vgorcinschi
 */
public class Java8Toolkit {
    public static Time findGoodTime(List<Time> times, Predicate<Time> t){
        for(Time time:times){
            if (t.test(time)) 
                return time;            
        }
        return null;
    }    
    
    /*
        Some explanations are required as to why I have decided to use 
        a deprecated constructor.
        1. Modern JDBC connectors do not support Java 8's LocalTime
        2. Using LocaTime's getLong(ChronoField.MILLI_OF_SECOND) method returns
        the same long value every time
    */
    public static java.sql.Time localToSqlTime(LocalTime localTime) {
        Function<LocalTime, java.sql.Time> sqlizer;
        sqlizer = (from) -> new java.sql.Time(from.getHour(), from.getMinute(), 0);
        return sqlizer.apply(localTime);
    }    
    
    public static java.sql.Date localToSqlDate(LocalDate localDate){
        Function<LocalDate, java.sql.Date> sqlizer;
        sqlizer = (from) -> new java.sql.Date(from.getYear(), from.getMonthValue(), from.getDayOfMonth());
        return sqlizer.apply(localDate);
    }
}
