/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.helpers.LockWrapper;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author vgorcinschi
 */
public class PropertiesProvider {

    private volatile static Properties uriProperties;

    public PropertiesProvider() {
    }

    public static Properties getUriProperties() {
        if (uriProperties == null) {
            try (LockWrapper aLock = new LockWrapper(new ReentrantLock(true))) {
                aLock.lock();
                if (uriProperties == null) {
                    uriProperties = new Properties();
                    try (final InputStream stream
                            = PropertiesProvider.class.getClass()
                            .getResourceAsStream(
                                    "/com/vgorcinschi/rimmanew/entproperties/uriinfo.properties")) {
                                uriProperties.load(stream);
                            } catch (NullPointerException npe) {
                                uriProperties.setProperty("scheme", "unknown");
                                uriProperties.setProperty("host", "unknown");
                                uriProperties.setProperty("port", "unknown");
                            }
                }
            } catch (Exception e) {
                //log the lock was not closed

            }
        }
        return uriProperties;
    }
}
