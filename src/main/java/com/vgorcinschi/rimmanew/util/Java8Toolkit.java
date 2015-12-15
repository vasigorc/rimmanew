/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author vgorcinschi
 */
public class Java8Toolkit {
    
    /*
        in order to not mutate the passed value the f(x) nextNotWeekEnd will
        create a copy of the localdate and change that copy prior to returning
        it
    */
    public static Function<LocalDate, LocalDate> nextNotWeekEnd = 
            (LocalDate l)->of(l.getYear(),l.getMonth(), l.getDayOfMonth())
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    
    public static Predicate<LocalDate> isAWeekEnd = (LocalDate l)->
            (l.getDayOfWeek()==DayOfWeek.SATURDAY||l.getDayOfWeek()==DayOfWeek.SUNDAY);
    
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
    
    /*
        we need to do -1900 for the year in java.sql.Date as it is the legacy start
        date for this class
        Similarly we have to do -1 for months as they start at 0.
    */
    public static java.sql.Date localToSqlDate(LocalDate localDate){
        Function<LocalDate, java.sql.Date> sqlizer;
        sqlizer = (from) -> new java.sql.Date(from.getYear()-1900, from.getMonthValue()-1, from.getDayOfMonth());
        return sqlizer.apply(localDate);
    }
    
    /*
        A Currying method to check if the passed localdate suits the predicate condition
    else submit it to function
    */
    public static LocalDate getNextSuitableDate
        (LocalDate l, Predicate<LocalDate> p, Function<LocalDate, LocalDate> f) {
        return (p.test(l)?f.apply(l):l);
    }
    
    public static java.util.Date localToUtilDate(LocalDate localDate){
        Function<LocalDate, java.util.Date> sqlizer;
        sqlizer = (from) -> new java.util.Date(from.getYear()-1900, from.getMonthValue()-1, from.getDayOfMonth());
        return sqlizer.apply(localDate);
    }
}
