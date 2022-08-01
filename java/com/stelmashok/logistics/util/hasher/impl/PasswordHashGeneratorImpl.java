package com.stelmashok.logistics.util.hasher.impl;

import com.stelmashok.logistics.util.hasher.PasswordHashGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class PasswordHashGeneratorImpl implements PasswordHashGenerator {

    private static final Logger logger = LogManager.getLogger();
    private static PasswordHashGeneratorImpl instance;

    private static final SecureRandom RAND = new SecureRandom();
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final String SALT = "sChHR5DLE1hb";

    private PasswordHashGeneratorImpl() {

    }

    public static PasswordHashGeneratorImpl getInstance() {
        if (instance == null) {
            instance = new PasswordHashGeneratorImpl();
        }
        return instance;
    }

    @Override
    public Optional<String> generatePasswordHash(String password) {

        char[] chars = password.toCharArray();
        byte[] bytes = SALT.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("failed to generate password hash", e);
            return Optional.empty();

        } finally {
            spec.clearPassword();
        }
    }

    @Override
    public boolean checkPasswordHash(String password, String key) {
        Optional<String> optEncrypted = generatePasswordHash(password);
        return optEncrypted.map(s -> s.equals(key)).orElse(false);
    }

    private String generateSalt() {
        byte[] salt = new byte[KEY_LENGTH];
        RAND.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
