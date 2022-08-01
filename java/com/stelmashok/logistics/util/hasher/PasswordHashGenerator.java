package com.stelmashok.logistics.util.hasher;

import java.util.Optional;

public interface PasswordHashGenerator {
    Optional<String> generatePasswordHash(String password);
    boolean checkPasswordHash(String password, String key);
}
