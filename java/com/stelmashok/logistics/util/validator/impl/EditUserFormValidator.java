package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.HashMap;
import java.util.Map;

public class EditUserFormValidator implements FormValidator {

    private static FormValidator instance;

    private EditUserFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new EditUserFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> data) {
        Map<String, String> validationResult = new HashMap<>();

        if (!data.get(NAME)[0].trim().matches(NAME_PATTERN)) {
            validationResult.put(NAME, MESSAGE_NAME_WRONG);
        }
        if (!data.get(EMAIL)[0].trim().matches(EMAIL_PATTERN)) {
            validationResult.put(EMAIL, MESSAGE_EMAIL_WRONG);
        }
        return validationResult;
    }
}
