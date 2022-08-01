package com.stelmashok.logistics.util.validator;

import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Map;

public interface UploadValidator {
    Map<String, String> validateUpload(List<Part> parts);
}
