/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

import java.util.concurrent.locks.Lock;

/**
 *
 * @author vgorcinschi
 * this is a class that can wrap any of the 8 implementations
 * of concurrent Lock. Class implements close() to be used in 
 * try-with-resources
 */
public class LockWrapper implements AutoCloseable{
    private final Lock _lock;

    public LockWrapper(Lock _lock) {
        this._lock = _lock;
    }
    
    public void lock(){
        this._lock.lock();
    }
    
    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
