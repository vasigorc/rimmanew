/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author vgorcinschi
 */
@ManagedBean
@Dependent
public class WeatherForecastBean implements VGObserver{
    private String dateRepresentation;

    @ManagedProperty(value = "#{appointmentFormBean}")
    private AppointmentFormBean formBean;
    /**
     * Creates a new instance of WeatherForecastBean
     */
    public WeatherForecastBean() {
        this.dateRepresentation = null;
    }

    //point of IoC
    public void setFormBean(AppointmentFormBean formBean) {
        this.formBean = formBean;
        formBean.registerVGObserver(this);
    }

    @Override
    public void update(Date selectedDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   // private java.sql.Date convertUtiltoSql()
    
}
