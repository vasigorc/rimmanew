/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import com.vgorcinschi.rimmanew.model.dailyforecast.DailyForecast;
import com.vgorcinschi.rimmanew.model.dailyforecast.TimeAdapter;
import com.vgorcinschi.rimmanew.model.dailyforecast.UnavailableForecast;
import com.vgorcinschi.rimmanew.rest.WeatherForecastClient;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import static com.vgorcinschi.rimmanew.util.DateConverters.utilToSql;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.findGoodTime;
import java.util.Calendar;

/**
 *
 * @author vgorcinschi
 */
@ManagedBean
@Dependent
public class WeatherForecastBean implements VGObserver{
    private String dateRepresentation;
    private final WeatherForecastClient wfc;
    private final DailyWeatherReport dwr;
    //this is the forecast object - shouldn't be final
    private DailyForecast forecast;

    @ManagedProperty(value = "#{appointmentFormBean}")
    private AppointmentFormBean formBean;
    /**
     * Creates a new instance of WeatherForecastBean
     */
    public WeatherForecastBean() {
        this.dateRepresentation = null;
        this.wfc = new WeatherForecastClient();
        //TODO replace the "fr" with the call to Locale
        this.dwr = wfc.getForecast(DailyWeatherReport.class, "fr");
    }

    //point of IoC
    public void setFormBean(AppointmentFormBean formBean) {
        this.formBean = formBean;
        formBean.registerVGObserver(this);
    }

    @Override
    public void update(Date selectedDate) {
        /*
            get the 15 days from today date object
            and check whether the client requested day is not 
            after it.
        */
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +15); 
        if (selectedDate.after(cal.getTime())) {
            /*
                So, if the selected date is after today+15 days
                then we will tell the client that we cannot grab a weathe for him
                i.e. DailyForecast = UnavailableForecast.class
            */
            setForecast(new UnavailableForecast());
        }else{
            /*
                Now that we know that the selected date is within the range
                of available forecasts, we need to extract the requested day 
                forecast (represented by a Time object) 
                from the total of 16 available days
            */
            java.sql.Date sqlDate = utilToSql(selectedDate);   
            Time requestedTime = findGoodTime(dwr.getDays(), 
                    (Time t)->sqlDate.toString().equals(t.getDay()));
            setForecast(new TimeAdapter(requestedTime));
        }        
    }

    public String getDateRepresentation() {
        return dateRepresentation;
    }

    public void setDateRepresentation(String dateRepresentation) {
        this.dateRepresentation = dateRepresentation;
    }

    public DailyForecast getForecast() {
        return forecast;
    }

    public void setForecast(DailyForecast forecast) {
        this.forecast = forecast;
    }
}
