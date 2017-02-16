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

    public void setFutureRespository(FutureAppointmentsRepository futureRespository) {
        this.futureRespository = futureRespository;
    }

    public void setCompanyProperties(CompanyProperties companyProperties) {
        this.companyProperties = companyProperties;
    }

    @Override
    @Schedule(second = "0", minute = "15", hour = "00", dayOfMonth = "*",
            month = "*", dayOfWeek = "Sat", year = "*", persistent = false)
    public void updatePastAppointments() {
        long setBefore = (long) companyProperties.getDaysBeforeMarkingAsPast();
        if (setBefore == -1) {
            log.error("Attempted to mark future appointments as passed");
        } else {
            Date markAsPassedFlag = Java8Toolkit.localToSqlDate(LocalDate.now()
                    .minusDays(setBefore));
            int result = futureRespository.batchSetIsPassedStatus(markAsPassedFlag);
            if (result < 0) {
                log.error("The status of past appointments has not been updated");
            } else {
                log.debug(result + " apointments have been successfully updated and "
                        + "marked as 'passed'. The most recent of them was scheduled"
                        + " for: " + markAsPassedFlag);
            }
        }
    }

    @Override
    @Schedule(second = "0", minute = "18", hour = "1", dayOfMonth = "*", month = "*", dayOfWeek = "Sat", year = "*",
            persistent = false)
    public void deleteArchaicAppointments() {
        long setBefore = (long) companyProperties.getDaysBeforeForceDeletingTheAppointmentRecord();
        if (setBefore < 30) {
            log.error("Attempted to delete appointments that are less then"
                    + " 30 days in the past.");
        } else {
            Date deleteBeforeFlag = Java8Toolkit.localToSqlDate(LocalDate.now()
                    .minusDays(setBefore));
            futureRespository.deleteAllBefore(deleteBeforeFlag);
        }
    }
}
