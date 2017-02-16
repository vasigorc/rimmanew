/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Credential;
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
public class OCCredentialRepository implements CredentialRepository {

    private final EntityManagerFactory entityManagerFactory;

    public OCCredentialRepository() {
        this.entityManagerFactory = EntityManagerFactoryProvider.getUniqueInstance();
    }

    @Override
    public boolean createCredential(Credential credential) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(credential);
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
    public boolean updateCredential(Credential credential) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(credential);
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
    public Credential getByUsername(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Credential> query = em.createQuery("SELECT c FROM "
                    + "Credential c WHERE LOWER(c.username) LIKE :username",
                    Credential.class).setParameter("username", username.toLowerCase());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Credential> getByGroups(String group) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Credential> query = em.createNamedQuery("findByGroup",
                    Credential.class).setParameter("group", group.toLowerCase());
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Credential> getActive() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Credential> query = em.createNamedQuery("findActiveCredentials",
                    Credential.class);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }

    }

    @Override
    public List<Credential> getAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Credential> query = em.createNamedQuery("findAllCredentials",
                    Credential.class);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

}
