/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Ideally we would be able to create an outside container, arquillian based,
 * test to test various aspects of the UserPreferencies CDI bean
 *
 * @author vgorcinschi
 */
@RunWith(Arquillian.class)
public class UserPreferenciesTests {

    public UserPreferenciesTests() {
    }

    //humble attempt to mock glassfish only with the UserPreferencies bean inside
    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(UserPreferencies.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return jar;
    }
    
    @Inject
    UserPreferencies preferencies;
    
    @Test
    public void localeIsNotNull(){
        assertNotNull(preferencies.getCurrentLocale().getCountry());
    }

    @Test
    public void defaultLocaleTest() {
        assertTrue("The default locale should be \"fr\"",
                "fr".equalsIgnoreCase(preferencies.getCurrentLocale().getLanguage()));
    }
}
