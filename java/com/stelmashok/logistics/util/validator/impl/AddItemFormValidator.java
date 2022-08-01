package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.HashMap;
import java.util.Map;

public class AddItemFormValidator implements FormValidator {

    private static FormValidator instance;

    private AddItemFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new AddItemFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> validationData) {

        Map<String, String> validationResult = new HashMap<>();

        //1-50, letters, digits, whitespaces, :.,-' symbols
        if (!validationData.get(TITLE)[0].trim().matches(TITLE_PATTERN)) {
            validationResult.put(TITLE, MESSAGE_TITLE_WRONG);
        }
        //1-10, digits
        if (!validationData.get(PRICE)[0].trim().matches(PRICE_PATTERN)) {
            validationResult.put(PRICE, MESSAGE_PRICE_WRONG);
        }
        //1-300, letters, digits, whitespaces, :!?.,_'- symbols
        if (!validationData.get(DESCRIPTION)[0].trim().matches(DESCRIPTION_PATTERN)) {
            validationResult.put(DESCRIPTION, MESSAGE_DESCRIPTION_WRONG);
        }
        //1-30, letters, digits, :.@'()+- symbols
        if (!validationData.get(CONTACT)[0].trim().matches(CONTACT_PATTERN)) {
            validationResult.put(CONTACT, MESSAGE_CONTACT_WRONG);
        }
        //1-30, letters, digits, whitespaces, '- symbols
        if (!validationData.get(CITY)[0].trim().matches(CITY_PATTERN)) {
            validationResult.put(CITY, MESSAGE_CITY_WRONG);
        }
        //1-30, letters, digits, whitespaces, :.'- symbols
        if (!validationData.get(CATEGORY)[0].trim().matches(CATEGORY_PATTERN)) {
            validationResult.put(CATEGORY, MESSAGE_CATEGORY_WRONG);
        }
        return validationResult;
    }
}
