/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.helpers.LockWrapper;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vgorcinschi
 * @see
 * http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string?page=1&tab=votes#tab-top
 * for why we use a singleton to get a secure random for our app "Note that
 * SecureRandom objects are expensive to initialize, so you'll want to keep one
 * around and reuse it."
 */
public class SecureRandomSingleton {

    private volatile static SecureRandom random;
    private static final Logger log = LogManager.getLogger();

    public SecureRandomSingleton() {
    }

    public static SecureRandom secureRandom() {
        if (random == null) {
            try (LockWrapper aLock = new LockWrapper(new ReentrantLock(true))) {
                aLock.lock();
                random = new SecureRandom();
            } catch (Exception e) {
                log.error("SecureRandomSingleton lock wasn't closed. It is no longer "
                        + "a singleton object. Exception: " + e.getMessage());
            }
        }
        return random;
    }
}
