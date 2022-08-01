package com.stelmashok.logistics.util.validator.impl;

import com.stelmashok.logistics.util.validator.FormValidator;

import java.util.HashMap;
import java.util.Map;

public class CommentFormValidator implements FormValidator {

    private static FormValidator instance;

    private CommentFormValidator() {
    }

    public static FormValidator getInstance() {
        if (instance == null) {
            instance = new CommentFormValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validateForm(Map<String, String[]> validationData) {
        Map<String, String> validationResult = new HashMap<>();
        //1-500, letters, digits, whitespaces, :!?.,_'- symbols
        if (!validationData.get(COMMENT_TEXT)[0].trim().matches(COMMENT_TEXT_PATTERN)) {
            validationResult.put(COMMENT_TEXT, MESSAGE_COMMENT_TEXT_WRONG);
        }
        return validationResult;
    }
}
