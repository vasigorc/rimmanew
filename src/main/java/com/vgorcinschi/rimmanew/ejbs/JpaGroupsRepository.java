/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.entities.Groups;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TransactionRequiredException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vgorcinschi
 */
@Singleton
@Production
public class JpaGroupsRepository implements GroupsRepository {

    private final Logger log = LogManager.getLogger();
    @PersistenceContext(unitName = "appointmentsManagement",
            type = PersistenceContextType.TRANSACTION)
    EntityManager em;

    @Override
    public boolean addGroup(Groups group) {
        try {
            em.persist(group);
            log.info("A new group has been saved: " + group);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateGroups(Groups group) {
        try {
            em.merge(group);
            return true;
        } catch (TransactionRequiredException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Groups getByGroupName(String groupName) {
        try {
            return em.createQuery("SELECT g FROM "
                    + "Groups g WHERE LOWER(g.groupName) LIKE :groupName", Groups.class)
                    .setParameter("groupName", groupName.toLowerCase()).getSingleResult();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Groups> getAll() {
        try {
            return em.createNamedQuery("findAllGroups", Groups.class).getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
