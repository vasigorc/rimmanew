/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author vgorcinschi
 */
public class ExecutorFactoryProvider{

    //we will be using a maximum of 15 concurrent threads
    private volatile static  Executor executorOf15 = Executors.newFixedThreadPool(15, (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });
    
    private volatile static Executor executorOf30 = Executors.newFixedThreadPool(30, (Runnable r)->{
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });
    
    public static Executor getSingletonExecutorOf15(){
        if (executorOf15 == null) {
            synchronized (ExecutorFactoryProvider.class) {
                if (executorOf15 == null) {
                    return executorOf15;
                }
            }
        }
        return executorOf15;
    }
    
    public static Executor getSingletonExecutorOf30(){
        if (executorOf30 == null) {            
            synchronized (ExecutorFactoryProvider.class) {
                if (executorOf30 == null) {
                    return executorOf30;
                }
            }
        }
        return executorOf30;
    }
}
