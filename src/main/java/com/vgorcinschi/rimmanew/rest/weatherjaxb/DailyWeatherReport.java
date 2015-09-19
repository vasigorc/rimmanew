/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.weatherjaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vgorcinschi
 */
@XmlRootElement(name = "weatherdata")
@XmlAccessorType(XmlAccessType.FIELD)
public class DailyWeatherReport {

    @XmlElement
    protected Location location;

    @XmlElement(name = "time")
    @XmlElementWrapper(name = "forecast")
    protected List<Time> days;

    public DailyWeatherReport() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Time> getDays() {
        if (this.days==null) {
            return new ArrayList();
        }
        return days;
    }

    public void setDays(List<Time> days) {
        this.days = days;
    }

}
