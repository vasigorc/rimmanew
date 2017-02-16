/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi;

import com.vgorcinschi.rimmanew.cdi.dailyforecast.DailyForecast;
import com.vgorcinschi.rimmanew.cdi.dailyforecast.TimeAdapter;
import com.vgorcinschi.rimmanew.cdi.dailyforecast.UnavailableForecast;
import com.vgorcinschi.rimmanew.rest.clients.WeatherForecastClient;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.DailyWeatherReport;
import com.vgorcinschi.rimmanew.rest.weatherjaxb.Time;
import static com.vgorcinschi.rimmanew.util.DateConverters.utilToSql;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.findGoodTime;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import static java.util.Optional.of;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "forecastBean")
@SessionScoped
public class ForecastBean implements Serializable {

    private final WeatherForecastClient wfc;
    private final DailyWeatherReport dwr;
    //this is the forecast object - shouldn't be final
    private DailyForecast forecast;
    private boolean beanActivated = false;
    private final Logger log = LogManager.getLogger();

    /**
     * Creates a new instance of ForecastBeanTwo
     */
    public ForecastBean() {
        this.wfc = new WeatherForecastClient();
        Optional<DailyWeatherReport> temporary = Optional.empty();
        try {
            //TODO replace the "fr" with the call to Locale
            temporary = of(wfc.getForecast(DailyWeatherReport.class, "fr"));
        } catch (UnknownHostException|javax.ws.rs.ProcessingException ex) {
            log.fatal("No access to the third party weather service: " + ex.getMessage());
        }
        if (temporary.isPresent()) {
            dwr = temporary.get();
        } else{
            dwr = null;
        }
    }

    public void handleDateSelect(SelectEvent event) {
        /*
         get the 15 days from today date object
         and check whether the client requested day is not 
         after it.
         */
        Date selectedDate = (Date) event.getObject();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +15);
        if (selectedDate.after(cal.getTime())) {
            /*
             So, if the selected date is after today+15 days
             then we will tell the client that we cannot grab a weathe for him
             i.e. DailyForecast = UnavailableForecast.class
             */
            setForecast(new UnavailableForecast().setReason("Unfortunatelly we "
                    + "are unable to provide weather forecast for that"
                    + " far in the future."));
        } else if(dwr==null){
            setForecast(new UnavailableForecast().setReason("We are sorry. "
                    + "The connection with the "
                    + "third party weather report vendor failed."));
        } else {
            /*
             Now that we know that the selected date is within the range
             of available forecasts, we need to extract the requested day 
             forecast (represented by a Time object) 
             from the total of 16 available days
             */
            java.sql.Date sqlDate = utilToSql(selectedDate);
            Time requestedTime = findGoodTime(dwr.getDays(),
                    (Time t) -> sqlDate.toString().equals(t.getDay()));
            setForecast(new TimeAdapter(requestedTime));
        }
        setBeanActivated(true);
    }

    public DailyForecast getForecast() {
        return forecast;
    }

    public void setForecast(DailyForecast forecast) {
        this.forecast = forecast;
    }

    public boolean isBeanActivated() {
        return beanActivated;
    }

    public void setBeanActivated(boolean beanActivated) {
        this.beanActivated = beanActivated;
    }
}
