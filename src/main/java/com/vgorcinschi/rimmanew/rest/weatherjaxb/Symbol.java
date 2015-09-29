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
public class Symbol {
    @XmlAttribute(name="name")
    protected String generally;

    public Symbol() {
    }

    public String getGenerally() {
        return generally;
    }

    public void setGenerally(String generally) {
        this.generally = generally;
    }
}
