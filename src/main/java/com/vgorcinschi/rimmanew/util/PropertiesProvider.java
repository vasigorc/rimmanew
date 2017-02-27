package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.helpers.LockWrapper;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vgorcinschi
 */
public class PropertiesProvider {

    private volatile static Properties uriProperties;
    private static final Logger log = LogManager.getLogger();

    public PropertiesProvider() {
    }

    public static Properties getUriProperties() {
        if (uriProperties == null) {
            try (LockWrapper aLock = new LockWrapper(new ReentrantLock(true))) {
                aLock.lock();
                if (uriProperties == null) {
                    uriProperties = new Properties();
                    try (final InputStream stream
                            = PropertiesProvider.class
                                    .getResourceAsStream("/com/vgorcinschi/rimmanew/entproperties/client.properties")) {
                                 uriProperties.load(stream);
                            } catch (NullPointerException npe) {
                                log.error("File 'client.properties' was not uploaded");
                                uriProperties.setProperty("scheme", "unknown");
                                uriProperties.setProperty("host", "unknown");
                                uriProperties.setProperty("port", "unknown");
                            }
                }
            } catch (Exception e) {
                //log the lock was not closed
                log.error("Uri Properties' lock wasn't closed. It is no longer "
                        + "a singleton object. Exception: "+e.getMessage());
            }
        }
        return uriProperties;
    }
}
