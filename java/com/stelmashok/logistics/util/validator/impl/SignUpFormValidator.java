package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.HashMap;
import java.util.Map;

public class SignUpFormValidator implements FormValidator {

    private static FormValidator instance;

    private SignUpFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new SignUpFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> validationData) {
        Map<String, String> validationResult = new HashMap<>();
        //1-30, letters, digits, whitespaces, :.'- symbols
        if (!validationData.containsKey(NAME) || !validationData.get(NAME)[0].trim().matches(NAME_PATTERN)) {
            validationResult.put(NAME, MESSAGE_NAME_WRONG);
        }
        //valid email such as name@gmail.com
        if (!validationData.containsKey(EMAIL) || !validationData.get(EMAIL)[0].trim().matches(EMAIL_PATTERN)) {
            validationResult.put(EMAIL, MESSAGE_EMAIL_WRONG);
        }
        //6-50, at least one digit one lower letter and one capital letter
        if (!validationData.containsKey(PASSWORD) || !validationData.get(PASSWORD)[0].trim().matches(PASSWORD_PATTERN)) {
            validationResult.put(PASSWORD, MESSAGE_PASSWORD_WRONG);
        }
        //6-50, at least one digit one lower letter and one capital letter
        if (!validationData.containsKey(REPEATED_PASSWORD) || !validationData.get(REPEATED_PASSWORD)[0].trim().matches(PASSWORD_PATTERN)) {
            validationResult.put(REPEATED_PASSWORD, MESSAGE_REPEATED_PASSWORD_WRONG);
        }
        //6-50, at least one digit one lower letter and one capital letter
        if (!validationData.get(REPEATED_PASSWORD)[0].trim().equals(validationData.get(PASSWORD)[0].trim())) {
            validationResult.put(REPEATED_PASSWORD, MESSAGE_PASSWORDS_DO_NOT_MATCH);
        }
        return validationResult;
    }
}
