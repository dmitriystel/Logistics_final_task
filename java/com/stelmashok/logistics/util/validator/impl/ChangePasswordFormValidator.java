package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFormValidator implements FormValidator {

    private static FormValidator instance;

    private ChangePasswordFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new ChangePasswordFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> data) {
        Map<String, String> validationResult = new HashMap<>();

        if (!data.get(OLD_PASSWORD)[0].trim().matches(PASSWORD_PATTERN)) {
            validationResult.put(OLD_PASSWORD, MESSAGE_PASSWORD_WRONG);
        }
        if (!data.get(NEW_PASSWORD)[0].trim().matches(PASSWORD_PATTERN)) {
            validationResult.put(NEW_PASSWORD, MESSAGE_PASSWORD_WRONG);
        }
        if (!data.get(REPEAT_NEW_PASSWORD)[0].trim().matches(PASSWORD_PATTERN)) {
            validationResult.put(REPEAT_NEW_PASSWORD, MESSAGE_REPEATED_PASSWORD_WRONG);
        }
        if (!data.get(NEW_PASSWORD)[0].trim().equals(data.get(REPEAT_NEW_PASSWORD)[0].trim())) {
            validationResult.put(REPEAT_NEW_PASSWORD, MESSAGE_PASSWORDS_DO_NOT_MATCH);
        }
        return validationResult;
    }
}
