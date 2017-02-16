/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author vgorcinschi
 */
@FacesValidator("inputHostValidator")
public class InputHostValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String stringValue = (String) value;
        if (stringValue.length()<8 || stringValue.length()>20) {
            FacesMessage message = com.vgorcinschi.rimmanew.util.
                    Messages
                    .getMessage("com.vgorcinschi.rimmanew.messagebundles.bigcopies", 
                            "validateHost", null);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
    
}
