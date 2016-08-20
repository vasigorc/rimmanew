/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Groups;
import com.vgorcinschi.rimmanew.util.EntityManagerFactoryProvider;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author vgorcinschi
 */
public class OCGroupsRepository implements GroupsRepository {

    private final EntityManagerFactory entityManagerFactory;

    public OCGroupsRepository() {
        this.entityManagerFactory = EntityManagerFactoryProvider.getUniqueInstance();
    }

    @Override
    public boolean addGroup(Groups group) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(group);
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
    public boolean updateGroups(Groups group) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(group);
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
    public Groups getByGroupName(String groupName) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Groups> query = em.createQuery("SELECT g FROM "
                    + "Groups g WHERE LOWER(g.groupName) LIKE :groupName",
                    Groups.class).setParameter("groupName", groupName.toLowerCase());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Groups> getAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Groups> query
                    = em.createNamedQuery("findAllGroups", Groups.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}
