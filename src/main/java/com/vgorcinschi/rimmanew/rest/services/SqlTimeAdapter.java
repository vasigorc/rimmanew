/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services;

import java.sql.Time;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author vgorcinschi
 */
public class SqlTimeAdapter extends XmlAdapter<String, Time>{

    @Override
    public Time unmarshal(String v) throws Exception {
        return Time.valueOf(v+":00");
    }

    @Override
    public String marshal(Time v) throws Exception {
        return v.toLocalTime().getHour()+":"+v.toLocalTime().getMinute();
    }
    
}
