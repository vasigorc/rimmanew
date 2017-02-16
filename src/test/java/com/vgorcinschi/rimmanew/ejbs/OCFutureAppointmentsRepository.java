/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.util.EntityManagerFactoryProvider;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author vgorcinschi
 */
public class OCFutureAppointmentsRepository implements FutureAppointmentsRepository {

    private final EntityManagerFactory entityManagerFactory;

    public OCFutureAppointmentsRepository() {
        this.entityManagerFactory = EntityManagerFactoryProvider.getUniqueInstance();
    }

    @Override
    public List<Appointment> getMarkedOngoingAndBeforeDate(Date date) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Appointment> query = em.createQuery("SELECT a FROM "
                    + "Appointment a WHERE a.date < :date AND"
                    + " a.past = :isPassed",
                    Appointment.class).setParameter("date", date)
                    .setParameter("isPassed", false);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void add(Appointment appointment) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(appointment);
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Appointment appointment) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(appointment);
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Appointment get(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Appointment app = em.find(Appointment.class, id);
            return app;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> getAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Appointment> query
                    = em.createNamedQuery("findFutureAppointments", Appointment.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> getByName(String name) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Appointment> query = em.createQuery("SELECT a FROM "
                    + "Appointment a WHERE LOWER(a.clientName) LIKE :custName"
                    + " AND a.past = false",
                    Appointment.class).setParameter("custName", name.toLowerCase());
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> getByDate(Date date) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Appointment> query
                    = em.createQuery("SELECT a FROM Appointment a "
                            + "WHERE a.date = :date", Appointment.class)
                    .setParameter("date", date);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> getByType(String type) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Appointment> query
                    = em.createQuery("SELECT a FROM Appointment a "
                            + "WHERE a.type = :type"
                            + " AND a.past = false", Appointment.class)
                    .setParameter("type", type);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Appointment getByDateAndTime(Date date, Time time) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Appointment> query
                    = em.createQuery("SELECT a FROM Appointment a "
                            + "WHERE a.date = :date AND a.time ="
                            + " :time", Appointment.class)
                    .setParameter("date", date).setParameter("time", time);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> getByDateAndType(Date date, String type) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Appointment> query
                    = em.createQuery("SELECT a FROM Appointment a WHERE a.date "
                            + "= :date AND a.type= :type", Appointment.class)
                    .setParameter("date", date)
                    .setParameter("type", type);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteOne(Appointment appointment) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.remove(em.merge(appointment));
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteAllBefore(Date date) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Query query = em.createQuery("DELETE FROM Appointment a WHERE "
                    + "a.date <= :date").setParameter("date", date);
            query.executeUpdate();
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public int batchSetIsPassedStatus(Date before) {
        EntityManager em = entityManagerFactory.createEntityManager();
        int affectedRows;
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Query query = em.createQuery("UPDATE Appointment a SET a.past = true"
                    + "  WHERE a.date <=:before"
                    + " AND a.past = false").setParameter("before", before);
            affectedRows = query.executeUpdate();
            trans.commit();
            return affectedRows;
        } catch (Exception e) {
            trans.rollback();
            return -1;
        } finally {
            em.close();
        }
    }
}
