/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.JpaRepository;
import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import java.time.LocalDate;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author vgorcinschi
 */
@Singleton
@JpaRepository
public class JpaSpecialDayRepository implements SpecialDayRepository {

    @PersistenceContext(unitName = "appointmentsManagement",
            type = PersistenceContextType.TRANSACTION)
    EntityManager em;

    @Override
    public boolean setSpecialDay(SpecialDay sd) {
        try {
            em.persist(sd);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public SpecialDay getSpecialDay(LocalDate ld) {
        try {
            return em.createQuery("SELECT s FROM SpecialDay s"
                    + " WHERE s.date = :date", SpecialDay.class)
                    .setParameter("date", Java8Toolkit.localToSqlDate(ld)).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean updateSpecialDay(SpecialDay sd) {
        try {
            em.merge(sd);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteSpecialDay(SpecialDay sd) {
        try {
            em.remove(em.merge(sd));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
