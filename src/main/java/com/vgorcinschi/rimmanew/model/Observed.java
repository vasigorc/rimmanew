/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

/**
 *
 * @author vgorcinschi
 */
public interface Observed {
    public void registerVGObserver(VGObserver o);
    public void removeVGObserver(VGObserver o);
    public void notifyVGObservers();
}
