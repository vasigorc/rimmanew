/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Credential;
import com.vgorcinschi.rimmanew.entities.Groups;
import com.vgorcinschi.rimmanew.util.EntityManagerFactoryProvider;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Groups getByGroupName(String groupName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Groups> getByCredential(Credential credential) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Groups> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
