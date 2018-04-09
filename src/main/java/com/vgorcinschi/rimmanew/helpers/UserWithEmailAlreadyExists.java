package com.vgorcinschi.rimmanew.helpers;

/**
 *
 * @author vgorcinschi
 */
public class UserWithEmailAlreadyExists extends IllegalArgumentException{

    public UserWithEmailAlreadyExists() {
    }

    public UserWithEmailAlreadyExists(String s) {
        super(s);
    }
}