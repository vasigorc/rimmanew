/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.helpers.NotAValidEmailException;
import com.vgorcinschi.rimmanew.helpers.UserWithEmailAlreadyExists;
import com.vgorcinschi.rimmanew.util.EntityManagerFactoryProvider;
import com.vgorcinschi.rimmanew.util.InputValidators;
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
        if(!emailIsAssigned(credential.getEmailAddress())){
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
        } else {
            throw new UserWithEmailAlreadyExists("User with email "
            +credential.getEmailAddress()+" already exists.");
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

    @Override
    public boolean emailIsAssigned(String... args) {
        assert(args.length > 0);
        if(InputValidators.isValidEmail.test(args[0])){
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                String query = "SELECT COUNT(c) FROM Credential c WHERE c.emailAddress "
                        + "= :email AND c.username ";
                TypedQuery<Long> count;
                if(args.length >1 && InputValidators.stringNotNullNorEmpty.apply(args[1])){
                    query = query.concat("!= :username");
                    count = em.createQuery(query, 
                    //only if the username has been provided
                        Long.TYPE).setParameter("email", args[0])
                        .setParameter("username", args[1]);
                } else {
                    query = query.concat("IS NOT NULL");
                    count = em.createQuery(query, 
                    //only if the username has been provided
                        Long.TYPE).setParameter("email", args[0]);
                }
                return count.getSingleResult() > 0;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                em.close();
            }
        }else{
            throw new NotAValidEmailException(args[0] + " is not a"
                    + " valid email address.");
        }
    }

    @Override
    public boolean delete(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.remove(getByUsername(username));
            trans.commit();
            return true;
        } catch (Exception e) {
            trans.rollback();
            return false;
        } finally {
            em.close();
        }
    }
}
