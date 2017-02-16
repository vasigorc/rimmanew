/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author vgorcinschi this is an implementation of Singleton Design Pattern. We
 * need just one instance of the EntityManagerFactoryProvider outside the GF
 * J2EE container to use it for testing.
 */
public class EntityManagerFactoryProvider {

    private volatile static EntityManagerFactory uniqueInstance;

    public EntityManagerFactoryProvider() {
    }

    public static EntityManagerFactory getUniqueInstance() {
        if (uniqueInstance == null) {
            synchronized (EntityManagerFactoryProvider.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = Persistence.createEntityManagerFactory("outsideContainer");
                }
            }
        }
        return uniqueInstance;
    }
}
