package com.stelmashok.logistics.util.validator;

import java.util.Map;

public interface FormValidator {
    Map<String, String> validateForm(Map<String, String[]> data);
}
