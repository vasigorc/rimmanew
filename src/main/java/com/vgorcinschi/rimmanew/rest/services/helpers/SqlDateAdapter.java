/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import java.sql.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author vgorcinschi
 */
public class SqlDateAdapter extends XmlAdapter<String, Date>{

    @Override
    public Date unmarshal(String v) throws Exception {
        return Date.valueOf(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return v.toLocalDate().toString();
    }
    
}
