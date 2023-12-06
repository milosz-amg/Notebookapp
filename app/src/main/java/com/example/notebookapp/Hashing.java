package com.example.notebookapp;

import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.generators.BCrypt;
import org.bouncycastle.crypto.params.KeyParameter;


public class Hashing {
    public static void debugMessage(){
        Log.i("T","test");
    }

    // Generowanie soli
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 10000;
        int keyLength = 256;

        //co bedziemy szyfrowac
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        //Hmac hash-based message authentication code
        //PBKDF2 Password-Based Key Derivation Function
        // popularne dla systemów mobilnych (IOS używa):
        //-potrzebuje mało mocy obliczeniowej - mało energii
        //-podatny na brute force
        //-trudno zaprogramować atak bruteforce na PBKDF2 na karcie graficznej
        byte[] hash = skf.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String userInput, String hashedPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // hashuj haslo podane
        String hashedUserInput = hashPassword(userInput, salt);

        // porownaj z hashem z sharedpreferecnes
        return hashedPassword.equals(hashedUserInput);
    }

    public static String encryptNote(String note, String key, byte[] salt) {
        try {
            int iterations = 10000;
            int keyLength = 256;
            //jak będziemy szyfrować
            //AES - advanced encription standard
            //OFB - output feedback
            //PKCS5 - jeżeli text jawny nie jest wielokrotnoscia 8 bitów padding
            Cipher cipher = Cipher.getInstance("AES/OFB/PKCS5Padding");

            // Wyznacz klucz z hasła i soli
//            PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), salt, iterations, keyLength);
//            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            //zamiast PBKDF2 użyć scrypt dla wzmocnienia??
//            byte[] derivedKey = skf.generateSecret(spec).getEncoded();
//            SecretKey secretKey = new SecretKeySpec(derivedKey, "AES");
//            WYZNACZANIE KLUCZA BCRYPT'EM
            byte[] keyBytes = key.getBytes();
            byte[] derivedKey = BCrypt.generate(keyBytes, salt, 12);
            SecretKey secretKey = new SecretKeySpec(derivedKey, "AES");

            // Wygeneruj losowy wektor inicjalizacyjny  -   podobnie jak sól
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] iv = new byte[16];
            random.nextBytes(iv);

            // Zainicjuj szyfrowanie kluczem i IV
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

            // Zaszyfruj notatkę
            byte[] encryptedBytes = cipher.doFinal(note.getBytes());

            // Połącz IV i zaszyfrowaną notatkę do przechowywania
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            //zakoduj do Stringa i zwróć
            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptNote(String encryptedNote, String key, byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/OFB/PKCS5Padding");

            // Zdekoduj połączony IV i zaszyfrowaną notatkę do tablicy bitowej
            byte[] combined = Base64.getDecoder().decode(encryptedNote);

            // Wyodrębnij IV
            byte[] iv = new byte[16];
            System.arraycopy(combined, 0, iv, 0, iv.length);

            // Wyodrębnij zaszyfrowaną notatkę
            byte[] encryptedBytes = new byte[combined.length - iv.length];
            System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

            // Wyznacz klucz z hasła i soli
//            PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), salt, 10000, 256);
//            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            byte[] derivedKey = skf.generateSecret(spec).getEncoded();
//            SecretKey secretKey = new SecretKeySpec(derivedKey, "AES");
            byte[] keyBytes = key.getBytes();
            byte[] derivedKey = BCrypt.generate(keyBytes, salt, 12);
            SecretKey secretKey = new SecretKeySpec(derivedKey, "AES");


            // Inicjuj szyfrowanie kluczem i IV
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            // Odszyfruj notatkę
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
