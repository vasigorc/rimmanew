/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import static java.util.Optional.ofNullable;

/**
 *builder for date internaionalized impl
 * check how all setters return @this 
 * which will allow for JavaScript style
 * instance creation
 * @author vgorcinschi
 */
public class InternationalizableDateBuilder {
    private Locale sessionLocale;
    private LocalDate localDate;
    private DayOfWeek dayOfWeek;
    private Month month;
    private TextStyle monthsStyle;
    private TextStyle dayOfWeekStyle;

    public InternationalizableDateBuilder(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Locale getSessionLocale() {
        return sessionLocale;
    }

    public InternationalizableDateBuilder setSessionLocale(Locale sessionLocale) {
        //if due to any reason a Locale cannot be returned we fall back on
        //french locale. We will need to log this.
        this.sessionLocale = ofNullable(sessionLocale).orElse(new Locale("fr"));
        return this;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public InternationalizableDateBuilder setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }   

    public TextStyle getMonthsStyle() {
        return monthsStyle;
    }

    public InternationalizableDateBuilder setMonthsStyle(TextStyle monthsStyle) {
        this.monthsStyle = monthsStyle;
        return this;
    }

    public TextStyle getDayOfWeekStyle() {
        return dayOfWeekStyle;
    }

    public InternationalizableDateBuilder setDayOfWeekStyle(TextStyle dayOfWeekStyle) {
        this.dayOfWeekStyle = dayOfWeekStyle;
        return this;
    }    
}
