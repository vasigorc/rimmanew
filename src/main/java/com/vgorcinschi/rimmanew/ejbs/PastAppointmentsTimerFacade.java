/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.JpaFutureRepository;
import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import java.sql.Date;
import java.time.LocalDate;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vgorcinschi
 */
@Stateless
public class PastAppointmentsTimerFacade implements PastAppointmentsTimerBeanLocal {

    private final Logger log = LogManager.getLogger();

    @Inject
    @JpaFutureRepository
    private FutureAppointmentsRepository futureRespository;

    @Inject
    @Production
    private CompanyProperties companyProperties;

    @Override
    //get all apps that are not "past" but are before NOW()
    //do foreach() to update() them with past="true"
    @Schedule(second = "0", minute = "0", hour = "18", dayOfMonth = "*", month = "*", dayOfWeek = "Sat", year = "*")
    public void updatePastAppointments() {
        long setBefore = (long) companyProperties.getDaysBeforeMarkingAsPast();
        if (setBefore == -1) {
            log.error("Attempted to mark future appointments as passed");
        } else {
            Date markAsPassedFlag = Java8Toolkit.localToSqlDate(LocalDate.now()
                    .minusDays(setBefore));
            int result = futureRespository.batchSetIsPassedStatus(markAsPassedFlag);
            if (result<0) {
                log.error("The status of past appointments has not been updated");
            } else{
                log.info(result+" apointments have been successfully updated and "
                        + "marked as 'passed'. The most recent of them was scheduled"
                        + " for: "+markAsPassedFlag);
            }
        }
    }

    @Override
    //do futureAppointmentRepository.deleteAllBefore(companyProperties.deleteAllBefore())    
    @Schedule(second = "0", minute = "0", hour = "23", dayOfMonth = "31", month = "12", dayOfWeek = "*", year = "*")
    public void deleteArchaicAppointments() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
