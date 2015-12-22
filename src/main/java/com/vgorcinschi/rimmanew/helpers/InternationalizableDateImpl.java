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
 *
 * @author vgorcinschi
 */
public class InternationalizableDateImpl implements InternationalizableDateTime{

    private Locale sessionLocale;
    private LocalDate localDate;
    private DayOfWeek dayOfWeek;
    private Month month;
    private TextStyle monthsStyle;
    private TextStyle dayOfWeekStyle;
    private InternationalizableDateBuilder builderObject;
    private int localDateYear, localDateDayOfMonth;

    public InternationalizableDateImpl(Locale sessionLocale, LocalDate localDate) {        
        this.sessionLocale = ofNullable(sessionLocale).orElse(new Locale("fr"));
        this.localDate = localDate;
    }    

    public InternationalizableDateImpl(InternationalizableDateBuilder builderObject) {
        this.builderObject = builderObject;
        this.sessionLocale = ofNullable(builderObject.getSessionLocale()).orElse(new Locale("fr"));
        this.localDate = builderObject.getLocalDate();
        this.dayOfWeek= this.localDate.getDayOfWeek();
        this.month=this.localDate.getMonth();
        this.localDateYear=this.localDate.getYear();
        this.localDateDayOfMonth=this.localDate.getDayOfMonth();
        //or Else Short will be our default
        this.monthsStyle=ofNullable(builderObject.getMonthsStyle()).orElse(TextStyle.SHORT);
        this.dayOfWeekStyle=ofNullable(builderObject.getDayOfWeekStyle()).orElse(TextStyle.SHORT);
    }
    
    public void setMonthsStyle(TextStyle monthsStyle) {
        this.monthsStyle = monthsStyle;
    }

    public void setDayOfWeekStyle(TextStyle dayOfWeekStyle) {
        this.dayOfWeekStyle = dayOfWeekStyle;
    }

    public TextStyle getMonthsStyle() {
        return monthsStyle;
    }

    public TextStyle getDayOfWeekStyle() {
        return dayOfWeekStyle;
    }
    
    @Override
    public String getWeekdayName() {
        return dayOfWeek.getDisplayName(dayOfWeekStyle, sessionLocale);
    }

    @Override
    public String getMonthName() {
        return month.getDisplayName(monthsStyle, sessionLocale).substring(0, 1).toUpperCase()+
                month.getDisplayName(monthsStyle, sessionLocale).substring(1);
    }

    public int getLocalDateYear() {
        return localDateYear;
    }

    public int getLocalDateDayOfMonth() {
        return localDateDayOfMonth;
    }
}
