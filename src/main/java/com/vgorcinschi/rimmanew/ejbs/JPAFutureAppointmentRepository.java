/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.JpaFutureRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author vgorcinschi
 */
@Singleton
@JpaFutureRepository
public class JPAFutureAppointmentRepository extends JpaAppointmentRepository
        implements FutureAppointmentsRepository {

    @Override
    public List<Appointment> getMarkedOngoingAndBeforeDate(Date date) {
        try {
            TypedQuery<Appointment> query = em.createQuery("SELECT a FROM "
                    + "Appointment a WHERE a.date < :date AND"
                    + " a.past = :isPassed",
                    Appointment.class).setParameter("date", date)
                    .setParameter("isPassed", false);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public int batchSetIsPassedStatus(Date before) {
        int affectedRows;
        try {
            Query query = em.createQuery("UPDATE Appointment a SET a.past = true"
                    + "  WHERE a.date <=:before"
                    + " AND a.past = false").setParameter("before", before);
            affectedRows = query.executeUpdate();
            return affectedRows;
        } catch (Exception e) {
            //LOG THIS!
            return -1;
        }
    }

    @Override
    public List<Appointment> getByType(String type) {
        try {
            return em.createQuery("SELECT a FROM Appointment a "
                    + "WHERE a.type = :type"
                    + " AND a.past = false", Appointment.class)
                    .setParameter("type", type).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Appointment> getByName(String name) {
        try {
            return em.createQuery("SELECT a FROM "
                    + "Appointment a WHERE LOWER(a.clientName) LIKE :custName"
                    + " AND a.past = false",
                    Appointment.class)
                    .setParameter("custName", name.toLowerCase()).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Appointment> getAll() {
        try {
            return em.createNamedQuery("findFutureAppointments", Appointment.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}
