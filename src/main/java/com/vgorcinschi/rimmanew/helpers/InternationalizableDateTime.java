/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

/**
 *We need a helper class that will allow
 * to @return internationalized values for
 * Java 8th Date & Time API
 * This is the interface for such a class
 * @author vgorcinschi
 */
public interface InternationalizableDateTime {
    String getWeekdayName();
    String getMonthName();
}
