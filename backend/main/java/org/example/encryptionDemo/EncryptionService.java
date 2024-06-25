package org.example.encryptionDemo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class EncryptionService {
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecretKey aesKey;

    public EncryptionService() throws Exception {
        // Generate AES key
        KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGen.init(AES_KEY_SIZE);
        this.aesKey = keyGen.generateKey();
    }

    public String encryptData(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmParameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        byte[] encryptedIvAndData = new byte[IV_LENGTH + encryptedData.length];
        System.arraycopy(iv, 0, encryptedIvAndData, 0, IV_LENGTH);
        System.arraycopy(encryptedData, 0, encryptedIvAndData, IV_LENGTH, encryptedData.length);

        return Base64.getEncoder().encodeToString(encryptedIvAndData);
    }

    public String decryptData(String encryptedData) throws Exception {
        byte[] encryptedIvAndData = Base64.getDecoder().decode(encryptedData);
        byte[] iv = new byte[IV_LENGTH];
        byte[] encryptedBytes = new byte[encryptedIvAndData.length - IV_LENGTH];

        System.arraycopy(encryptedIvAndData, 0, iv, 0, IV_LENGTH);
        System.arraycopy(encryptedIvAndData, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmParameterSpec);

        byte[] decryptedData = cipher.doFinal(encryptedBytes);
        return new String(decryptedData);
    }

    public SecretKey getAesKey() {
        return aesKey;
    }
}

