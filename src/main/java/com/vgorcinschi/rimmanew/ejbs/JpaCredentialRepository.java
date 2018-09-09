/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.helpers.NotAValidEmailException;
import com.vgorcinschi.rimmanew.helpers.UserWithEmailAlreadyExists;
import com.vgorcinschi.rimmanew.util.InputValidators;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.ejb.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
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
        if(!emailIsAssigned(credential.getEmailAddress())){
            //only then we bother
            try {
                em.persist(credential);
                log.info("A new user has been saved: " + credential);
                return true;
            } catch (EntityExistsException | TransactionRequiredException | IllegalArgumentException e) {
                log.error(e.getMessage());
                return false;
            }
        } else {
            throw new UserWithEmailAlreadyExists("User with email "
            +credential.getEmailAddress()+" already exists.");
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
                    + "Credential c WHERE c.username = :username", Credential.class)
                    .setParameter("username", username).getSingleResult();
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

    @Override
    public boolean emailIsAssigned(String... args) {
        assert(args.length > 0);
        if(InputValidators.isValidEmail.test(args[0])){
            Long nrOfOccurences = Long.valueOf(0);
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) "
                        + "FROM Credential c WHERE c.emailAddress "
                        + "= :email AND c.username != :username", 
                    Long.TYPE).setParameter("email", args[0])
                    .setParameter("username", null);
            if(args.length > 1 && InputValidators.stringNotNullNorEmpty.apply(args[1])){
                //only if the username has been provided
                query.setParameter("username", args[1]);
            }
            nrOfOccurences = query.getSingleResult();
            return nrOfOccurences > 0;
        } else {
            throw new NotAValidEmailException(Arrays.toString(args) + " is not a"
                    + " valid email address.");
        }
    }

    @Override
    public boolean delete(final String username) {
        return Optional.ofNullable(getByUsername(username)).map((cr) -> {
            em.remove(cr);
            return Boolean.TRUE;
        }
        ).orElseThrow(() -> new IllegalArgumentException(String.format("User %s doesn't exist", username)));
    }
}