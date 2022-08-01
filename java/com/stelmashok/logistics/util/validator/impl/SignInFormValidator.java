package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.HashMap;
import java.util.Map;

public class SignInFormValidator implements FormValidator {

    private static FormValidator instance;

    private SignInFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new SignInFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> validationData) {

        Map<String, String> validationResult = new HashMap<>();

        if (!validationData.get(EMAIL)[0].trim().matches(EMAIL_PATTERN)) {
            validationResult.put(EMAIL, MESSAGE_EMAIL_WRONG);
        }
        if (!validationData.get(PASSWORD)[0].trim().matches(PASSWORD_PATTERN)) {
            validationResult.put(PASSWORD, MESSAGE_PASSWORD_WRONG);
        }

        return validationResult;
    }
}
