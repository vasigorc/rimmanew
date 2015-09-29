/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.weatherjaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author vgorcinschi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WindSpeed {
    @XmlAttribute
    protected String mps;
    
    @XmlAttribute(name="name")
    protected String windDescr;

    public WindSpeed() {
    }

    public String getMps() {
        return mps;
    }

    public void setMps(String mps) {
        this.mps = mps;
    }

    public String getWindDescr() {
        return windDescr;
    }

    public void setWindDescr(String windDescr) {
        this.windDescr = windDescr;
    }
}
