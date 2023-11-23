package com.example.notebookapp;

import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hashing {
    public static void debugMessage(){
        Log.i("T","test");
    }

    // Generowanie soli
    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 10000; // You can adjust the number of iterations
        int keyLength = 256; // You can adjust the key length

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(hash);
    }

    private static boolean verifyPassword(String userInput, String hashedPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Hash the user input with the stored salt
        String hashedUserInput = hashPassword(userInput, salt);

        // Compare the stored hash with the hash of the user input
        return hashedPassword.equals(hashedUserInput);
    }





}
