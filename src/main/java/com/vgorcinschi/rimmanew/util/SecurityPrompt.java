/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import static com.vgorcinschi.rimmanew.util.SecureRandomSingleton.secureRandom;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author vgorcinschi
 */
public class SecurityPrompt {
    /*
     works by choosing 130 bits from a cryptographically secure 
     random bit generator, and encoding them in base-32. 
     128 bits is considered to be cryptographically strong, but each digit 
     in a base 32 number can encode 5 bits, so 128 is rounded up to the next 
     multiple of 5. This encoding is compact and efficient, with 5 random 
     bits per character. 
     */
    public static final int ITERATIONS_COUNT = 120000;
    public static final int KEY_LENGTH = 512;

    public static byte[] randomSalt() {
        return new BigInteger(130, secureRandom()).toString(32).getBytes(StandardCharsets.UTF_8);
    }

    /*
        iterationCount 120 000
        keyLength 512
    */
    public static byte[] pbkdf2(final String password, final byte[] salt, final int iterationCount, 
            final int keyLength) {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                    .generateSecret(
                            new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength)
                    ).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
