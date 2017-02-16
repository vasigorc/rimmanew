/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import java.time.LocalTime;

/**
 *
 * @author vgorcinschi
 */
public class DateConverters {

    public static java.sql.Date utilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
    
    public static LocalTime getLocalTimeRepr(java.sql.Time time){
        return time.toLocalTime();
    }
}
