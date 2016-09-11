/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.cdi;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CredentialRepository;
import com.vgorcinschi.rimmanew.entities.Credential;
import static com.vgorcinschi.rimmanew.util.SecurityPrompt.ITERATIONS_COUNT;
import static com.vgorcinschi.rimmanew.util.SecurityPrompt.KEY_LENGTH;
import static com.vgorcinschi.rimmanew.util.SecurityPrompt.pbkdf2;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Arrays;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author vgorcinschi
 */
@Named(value = "login")
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    @Production
    private CredentialRepository repository;

    private String usernameField;
    private String passwordField;
    private Credential credential;
    private int loginAttemptsCount;
    private final org.apache.logging.log4j.Logger log = LogManager.getLogger();

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        usernameField = passwordField = "";
        loginAttemptsCount = 0;
        credential = null;
    }

    public String getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(String usernameField) {
        this.usernameField = usernameField;
    }

    public String getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(String passwordField) {
        this.passwordField = passwordField;
    }

    public int getLoginAttemptsCount() {
        return loginAttemptsCount;
    }

    public void setLoginAttemptsCount(int loginAttemptsCount) {
        this.loginAttemptsCount = loginAttemptsCount;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public String authenticate() {
        loginAttemptsCount++;
        Credential unchecked = repository.getByUsername(getUsernameField());
        if (unchecked == null) {
            FacesMessage message = com.vgorcinschi.rimmanew.util.Messages
                    .getMessage("com.vgorcinschi.rimmanew.messagebundles.bigcopies",
                            "authenticationFailure", null);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        byte[] attemptedPassword = pbkdf2(getPasswordField(), unchecked.getSalt(), ITERATIONS_COUNT, KEY_LENGTH);
        if (!Arrays.equals(attemptedPassword, unchecked.getPasswd())) {
            FacesMessage message = com.vgorcinschi.rimmanew.util.Messages
                    .getMessage("com.vgorcinschi.rimmanew.messagebundles.bigcopies",
                            "authenticationFailure", null);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        credential = unchecked;
        return "permit-admin";
    }

    public void forgotPassword() {
    }
}
