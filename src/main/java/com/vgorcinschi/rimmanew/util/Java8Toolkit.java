/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.entities.DivizableDay;
import com.vgorcinschi.rimmanew.helpers.TriFunction;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import static com.vgorcinschi.rimmanew.util.ExecutorFactoryProvider.getSingletonExecutorOf30;
import static java.lang.Integer.parseInt;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.Duration;
import static java.time.Duration.between;
import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;
import javax.ws.rs.core.UriBuilder;

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
    public static Function<LocalDate, LocalDate> nextNotWeekEnd
            = (LocalDate l) -> of(l.getYear(), l.getMonth(), l.getDayOfMonth())
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

    public static Predicate<LocalDate> isAWeekEnd = (LocalDate l)
            -> (l.getDayOfWeek() == DayOfWeek.SATURDAY || l.getDayOfWeek() == DayOfWeek.SUNDAY);

    public static Time findGoodTime(List<Time> times, Predicate<Time> t) {
        for (Time time : times) {
            if (t.test(time)) {
                return time;
            }
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
    public static java.sql.Date localToSqlDate(LocalDate localDate) {
        Function<LocalDate, java.sql.Date> sqlizer;
        sqlizer = (from) -> new java.sql.Date(from.getYear() - 1900, from.getMonthValue() - 1, from.getDayOfMonth());
        return sqlizer.apply(localDate);
    }

    /*
     A Currying method to check if the passed localdate suits the predicate condition
     else submit it to function
     */
    public static LocalDate getNextSuitableDate(LocalDate l, Predicate<LocalDate> p, Function<LocalDate, LocalDate> f) {
        return (p.test(l) ? f.apply(l) : l);
    }

    public static java.util.Date localToUtilDate(LocalDate localDate) {
        Function<LocalDate, java.util.Date> sqlizer;
        sqlizer = (from) -> new java.util.Date(from.getYear() - 1900, from.getMonthValue() - 1, from.getDayOfMonth());
        return sqlizer.apply(localDate);
    }

    //this is not used, the recursive method below proved faster (88 msecs vs 0 msecs)
    public static List<LocalTime> durationSplitr(LocalTime l1, LocalTime l2, Duration d) {
        List<LocalTime> avails = new LinkedList<>();
        //LocalTime is an immutable class, so we don't need to clone the original
        //imported object to maintain the referential transparency
        LocalTime sliding = l1;
        Duration intermediary = between(sliding, l2);
        while (d.toMinutes() <= intermediary.toMinutes()) {
            avails.add(sliding);
            sliding = sliding.plusMinutes(d.toMinutes());
            intermediary = between(sliding, l2);
        }
        return avails;
    }

    public static List<LocalTime> recursiveDurationSplitr(Duration d, LocalTime l1,
            LocalTime l2) {
        List<LocalTime> list = new LinkedList<>();
        return durationSplitrHelper(d, l1, l2, list);
    }

    public static List<LocalTime> durationSplitrHelper(Duration d, LocalTime l1,
            LocalTime l2, List<LocalTime> list) {
        if (d.toMinutes() <= between(l1, l2).toMinutes()) {
            list.add(l1);
            return durationSplitrHelper(d, l1.plusMinutes(d.toMinutes()), l2, list);
        } else {
            return list;
        }
    }

    //if either BreakStart or BreakEnd are null then we consider that
    //there is no break in the day
    public static Predicate<DivizableDay> noBreakInSchedule = (DivizableDay dd)
            -> dd.getBreakStart() == null || dd.getBreakEnd() == null;
    /**
     * a static TriFunction that takes as @args: 1. an implementation of
     * DivizableDay(getMethods for a normal or special schedule) 2. a list of
     * already booked times (LocalTimes to be precise) 3. a Predicate that will
     * test whether there is a break in the day or not return a list of
     * availabilities (LocalTimes)
     */
    public static TriFunction<DivizableDay, List<Appointment>, Predicate<DivizableDay>, List<LocalTime>> getAvailabilitiesPerWorkingDay
            = (DivizableDay dd, List<Appointment> appsList, Predicate<DivizableDay> predicate)
            -> {
                //initializing the list that will be returned (not shared
                //with other threads, nor passed to other methods)
                @SuppressWarnings("UnusedAssignment")
                List<LocalTime> avails = new LinkedList<>();
                if (predicate.test(dd)) {
                    avails = recursiveDurationSplitr(dd.getDuration(), dd.getStartAt(), dd.getEndAt());
                } else {
                    //querying all availabilities before noon in a separate thread
                    Future<List<LocalTime>> beforeNoon
                    = CompletableFuture.supplyAsync(() -> recursiveDurationSplitr(dd.getDuration(), dd.getStartAt(), dd.getBreakStart()),
                            getSingletonExecutorOf30());
                    avails = recursiveDurationSplitr(dd.getDuration(), dd.getBreakEnd(), dd.getEndAt());
                    try {
                        avails.addAll(beforeNoon.get(1500, TimeUnit.MILLISECONDS));
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        //TODO logging has to go here
                    }
                }
                //subtracting from avails LocalTimes mapped from appsList
                avails.removeAll(appsList.stream().map(a -> a.getTime().toLocalTime()).collect(toList()));
                return avails.stream().sorted().collect(toList());
            };
    public static Supplier<UriBuilder> appsUriBuilder = () -> {
        Properties props = PropertiesProvider.getUriProperties();
        UriBuilder uriBuilder = UriBuilder.fromPath("RimmaNew/rest").scheme(props.getProperty("scheme"))
                .host(props.getProperty("host"));
        if (props.getProperty("port") == null || props.getProperty("port").equals("")) {
            return uriBuilder;
        } else {
            return uriBuilder.port(parseInt(props.getProperty("port")));
        }

    };

    public static BiFunction<Supplier<UriBuilder>, Map<String, String>, URI> uriGenerator
            = (supplier, map) -> {
                UriBuilder clone = supplier.get().clone();
                map.forEach((k, v) -> {
                    if (k.equals("path")) {
                        clone.path(map.get("path"));
                    } else {
                        clone.queryParam(k, v);
                    }
                });
                return clone.build();
            };

    public static Function<Field, String> genericTypeIdentifier
            = (generic) -> {
                ParameterizedType stringListType = (ParameterizedType) generic.getGenericType();
                Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                return stringListClass.getTypeName();
            };
}
