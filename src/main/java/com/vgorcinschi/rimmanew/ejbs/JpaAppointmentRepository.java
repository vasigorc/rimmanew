/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.JpaRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vgorcinschi
 */
@Singleton
@JpaRepository
public class JpaAppointmentRepository implements AppointmentRepository {

    private final Logger log = LogManager.getLogger();
    @PersistenceContext(unitName = "appointmentsManagement",
            type = PersistenceContextType.TRANSACTION)
    EntityManager em;

    @Override
    public void add(Appointment appointment) {
        em.persist(appointment);
    }

    @Override
    public void update(Appointment appointment) {
        em.merge(appointment);
    }

    @Override
    public Appointment get(long id) {
        try {
            return this.em.createQuery("SELECT a FROM Appointment a WHERE a.id = :id",
                    Appointment.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Appointment> getAll() {
        try {
            return em.createNamedQuery("findAllAppointments", Appointment.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Appointment> getByName(String name) {
        try {
            return em.createQuery("SELECT a FROM "
                    + "Appointment a WHERE LOWER(a.clientName) LIKE :custName",
                    Appointment.class)
                    .setParameter("custName", name.toLowerCase()).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Appointment> getByDate(Date date) {
        try {
            return em.createQuery("SELECT a FROM Appointment a "
                    + "WHERE a.date = :date", Appointment.class)
                    .setParameter("date", date).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Appointment> getByType(String type) {
        try {
            return em.createQuery("SELECT a FROM Appointment a "
                    + "WHERE a.type = :type", Appointment.class)
                    .setParameter("type", type).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Appointment getByDateAndTime(Date date, Time time) {
        try {
            return em.createQuery("SELECT a FROM Appointment a "
                    + "WHERE a.date = :date AND a.time ="
                    + " :time", Appointment.class)
                    .setParameter("date", date).setParameter("time", time).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Appointment> getByDateAndType(Date date, String type) {
        try {
            return em.createQuery("SELECT a FROM Appointment a WHERE a.date "
                                + "= :date AND a.type= :type", Appointment.class)
                        .setParameter("date", date)
                        .setParameter("type", type).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteOne(Appointment appointment) {
        em.remove(em.merge(appointment));
    }

    @Override
    public void deleteAllBefore(Date date) {
        int result=em.createQuery("DELETE FROM Appointment a WHERE "
                        + "a.date <= :date").setParameter("date", date).executeUpdate();
        if (result<0) {
                log.debug("No appointments were permanently deleted from your system"
                        + " as the outcome of a scheduled job. The attempt was"
                        + " to delete all appointments on or before "+date.toString());
            } else{
                log.debug(result+" apointment(s) have been permanently deleted from "
                        + "the system. The most recent of them was scheduled"
                        + " for: "+date.toString());
            }
    }

}
