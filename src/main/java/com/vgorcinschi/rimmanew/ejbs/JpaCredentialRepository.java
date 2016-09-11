/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.entities.Credential;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class JpaCredentialRepository implements CredentialRepository {

    private final Logger log = LogManager.getLogger();
    @PersistenceContext(unitName = "appointmentsManagement",
            type = PersistenceContextType.TRANSACTION)
    EntityManager em;

    @Override
    public boolean createCredential(Credential credential) {
        try {
            em.persist(credential);
            log.info("A new user has been saved: " + credential);
            return true;
        } catch (EntityExistsException | TransactionRequiredException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCredential(Credential credential) {
        try {
            em.merge(credential);
            log.info("User has been updated: " + credential);
            return true;
        } catch (TransactionRequiredException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Credential getByUsername(String username) {
        try {
            return em.createQuery("SELECT c FROM "
                    + "Credential c WHERE LOWER(c.username) LIKE :username", Credential.class)
                    .setParameter("username", username.toLowerCase()).getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Credential> getByGroups(String group) {
        try {
            return em.createNamedQuery("findByGroup", Credential.class)
                    .setParameter("group", group.toLowerCase()).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Credential> getActive() {
        try {
            return em.createNamedQuery("findActiveCredentials",
                    Credential.class).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Credential> getAll() {
        try {
            return em.createNamedQuery("findAllCredentials",
                    Credential.class).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
