/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.ejbs.SpecialDayRepository;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.util.EntityManagerFactoryProvider;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author vgorcinschi
 */
public class OutsideContainerSpecialDayRepository implements SpecialDayRepository {

    EntityManagerFactory entityManagerFactory;

    public OutsideContainerSpecialDayRepository() {
        this.entityManagerFactory = EntityManagerFactoryProvider.getUniqueInstance();
    }

    @Override
    public boolean setSpecialDay(SpecialDay sd) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(sd);
            trans.commit();
            return true;
        } catch (Exception e) {
            trans.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public SpecialDay getSpecialDay(LocalDate ld) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM SpecialDay s"
                    + " WHERE s.date = :date", SpecialDay.class)
                    .setParameter("date", Java8Toolkit.localToSqlDate(ld)).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean updateSpecialDay(SpecialDay sd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteSpecialDay(SpecialDay sd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SpecialDay> getAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<SpecialDay> query
                    = em.createQuery("SELECT s FROM SpecialDay s order by s.date DESC",
                            SpecialDay.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}
