package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.HashMap;
import java.util.Map;

public class AddCityFormValidator implements FormValidator {

    private static FormValidator instance;

    private AddCityFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new AddCityFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> data) {
        Map<String, String> validationResult = new HashMap<>();
        if (!data.get(CITY_TITLE)[0].trim().matches(CITY_TITLE_PATTERN)) {
            validationResult.put(TITLE, MESSAGE_TITLE_WRONG);
        }
        return validationResult;
    }
}
