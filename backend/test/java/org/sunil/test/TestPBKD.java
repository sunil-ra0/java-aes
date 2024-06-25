package org.sunil.test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class TestPBKD {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    public static SecretKeySpec getKeyFromPassword(String password, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static void main(String[] args) throws Exception {
        String password = "myStrongPassword";
        String salt = "myUniqueSalt"; // This should be unique to each alliance partner / user

        SecretKeySpec key1 = TestPBKD.getKeyFromPassword(password, salt);
        SecretKeySpec key2 = TestPBKD.getKeyFromPassword(password, salt);

        // Both keys should be the same
        System.out.println(Base64.getEncoder().encodeToString(key1.getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(key2.getEncoded()));
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }


}