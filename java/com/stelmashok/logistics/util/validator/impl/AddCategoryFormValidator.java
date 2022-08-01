package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.Map;

public class AddCategoryFormValidator implements FormValidator {

    private static FormValidator instance;

    private AddCategoryFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new AddCategoryFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> data) {
        Map<String, String> validationResult = new HashMap<>();
        if (!data.get(CATEGORY_TITLE)[0].trim().matches(CATEGORY_TITLE_PATTERN)) {
            validationResult.put(TITLE, MESSAGE_TITLE_WRONG);
        }
        return validationResult;
    }
}
