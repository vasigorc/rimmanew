/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.model.CompanyPropertiesBean;
import java.util.Properties;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class UriPropertiesTests {

    private final CompanyPropertiesBean propertiesBean;

    public UriPropertiesTests() {
        this.propertiesBean = new CompanyPropertiesBean();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void propertiesFileIsReadAndResetPropsTests() {
        Properties props = PropertiesProvider.getUriProperties();
        assertTrue(props.getProperty("host").equals("localhost"));
        System.out.println("\nDefault properties:\n\nHost is: " + props.getProperty("host")
                + "\nScheme is: " + props.getProperty("scheme")
                + "\nListening on port: " + Integer.parseInt(props.getProperty("port")));
        
        //then try to reset the values with the application scoped bean
        propertiesBean.setHostName("www.salon-rimma.ca");
        propertiesBean.setSchemeName("http");
        propertiesBean.setPort(0);
        propertiesBean.updateUriProperties();
        assertTrue(props.getProperty("host").equals("www.salon-rimma.ca"));
        System.out.println("\nUpdated properties:\n\n"
                + "Host is: " + props.getProperty("host")
                + "\nScheme is: " + props.getProperty("scheme"));
        if (props.getProperty("port").equals("")) {
            System.out.println("No port provided");
        } else {
            System.out.println("Listening on port: " + Integer.parseInt(props.getProperty("port")));
        }
    }
}
