/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import java.util.List;
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
}
