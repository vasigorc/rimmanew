/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author vgorcinschi
 */
@RunWith(value=org.junit.runners.Suite.class)
@SuiteClasses(value={AppointmentCDIMockTests.class, CDIIntegrationTesting.class,
InjectionTesting.class, ServiceMethodsTests.class})
public class InmutableInMemoryRepositoryTestsSuite {
    
    public InmutableInMemoryRepositoryTestsSuite() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

}
