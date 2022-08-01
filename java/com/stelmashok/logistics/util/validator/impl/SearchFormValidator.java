package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.Map;

public class SearchFormValidator implements FormValidator {

    private static FormValidator instance;

    private SearchFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new SearchFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> data) {
        Map<String, String> validationResult = new HashMap<>();
        if (!data.get(SEARCH_QUERY)[0].trim().matches(TITLE_PATTERN)) {
            validationResult.put(SEARCH_QUERY, MESSAGE_SEARCH_QUERY_WRONG);
        }
        return validationResult;
    }
}
