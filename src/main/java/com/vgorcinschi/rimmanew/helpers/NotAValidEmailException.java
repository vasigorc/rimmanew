package com.vgorcinschi.rimmanew.helpers;

/**
 * is thrown when a input validation fails
 * for an email address candidate
 * @author vgorcinschi
 */
public class NotAValidEmailException extends IllegalArgumentException{

    public NotAValidEmailException() {
    }

    public NotAValidEmailException(String s) {
        super(s);
    }
}
