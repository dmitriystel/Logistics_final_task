package com.stelmashok.logistics.util.hasher.impl;

import com.stelmashok.logistics.util.hasher.UserHashGenerator;
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

public class UserHashGeneratorImpl implements UserHashGenerator {

    private static final Logger logger = LogManager.getLogger();
    private static final SecureRandom RAND = new SecureRandom();
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static UserHashGeneratorImpl instance;

    private UserHashGeneratorImpl() {
    }

    public static UserHashGeneratorImpl getInstance() {
        if (instance == null) {
            instance = new UserHashGeneratorImpl();
        }
        return instance;
    }

    @Override
    public Optional<String> generateUserHash(String email) {
        char[] chars = email.toCharArray();
        byte[] bytes = generateSalt().getBytes();
        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);
        Arrays.fill(chars, Character.MIN_VALUE);
        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("failed to generate user hash", e);
            return Optional.empty();
        } finally {
            spec.clearPassword();
        }
    }

    private String generateSalt() {
        byte[] salt = new byte[KEY_LENGTH];
        RAND.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
