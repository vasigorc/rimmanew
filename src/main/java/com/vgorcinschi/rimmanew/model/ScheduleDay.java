/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author vgorcinschi
 */
public interface ScheduleDay {
    boolean isBlocked();
    Optional<List<LocalTime>> getSlots();
}
