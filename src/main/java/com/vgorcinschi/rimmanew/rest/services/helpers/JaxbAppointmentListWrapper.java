/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.vgorcinschi.rimmanew.entities.Appointment;
import java.util.List;

/**
 *
 * @author vgorcinschi
 */
@JsonRootName(value="appointments")
public class JaxbAppointmentListWrapper extends GenericBaseJaxbListWrapper<Appointment>{

   public JaxbAppointmentListWrapper(List<Appointment> current) {
        super(current);
    }
}
