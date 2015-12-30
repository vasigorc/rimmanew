/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

/**
 *
 * @author vgorcinschi
 * a simple interface that we will use to implement
 * a Chain of Responsibility to get:
 * 1. is BlockedDay
 * 2. is SpecialDay
 * 3. then it is Normal Day
 */
public interface ScheduleHandler {
    void handleRequest();
}
