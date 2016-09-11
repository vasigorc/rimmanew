/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.ejbs.CompanyPropertiesImpl;
import com.vgorcinschi.rimmanew.cdi.UriSetterBean;
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

    private final UriSetterBean uriPropertiesBean;
    private Properties props;

    public UriPropertiesTests() {
        this.uriPropertiesBean = new UriSetterBean();
        this.uriPropertiesBean.setCompanyProperties(new CompanyPropertiesImpl());
    }

    @Before
    public void setUp() {
        //need to put it here to escape the deadlock
        props = PropertiesProvider.getUriProperties();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void propertiesFileIsReadAndResetPropsTests() {
        assertTrue(props.getProperty("host").equals("localhost"));
        System.out.println("\nDefault properties:\n\nHost is: " + props.getProperty("host")
                + "\nScheme is: " + props.getProperty("scheme")
                + "\nListening on port: " + Integer.parseInt(props.getProperty("port")));
        
        //then try to reset the values with the application scoped bean
        uriPropertiesBean.setHostName("www.salon-rimma.ca");
        uriPropertiesBean.setSchemeName("http");
        uriPropertiesBean.setPort(0);
        uriPropertiesBean.updateUriProperties();
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
