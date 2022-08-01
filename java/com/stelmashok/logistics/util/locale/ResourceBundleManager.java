package com.stelmashok.logistics.util.locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ResourceBundle;

public class ResourceBundleManager {

    private static final Logger logger = LogManager.getLogger();

    private static final String DEFAULT_LOCALIZATION = "en";

    private static ResourceBundleManager instance;

    private ResourceBundleManager() {
    }

    public static ResourceBundleManager getInstance() {
        if (instance == null) {
            instance = new ResourceBundleManager();
        }
        return instance;
    }

    public ResourceBundle getResourceBundle(String selectedLanguage) {
        ResourceBundle resourceBundle;
        if (selectedLanguage != null) {
            try {
                resourceBundle = ResourceBundle.getBundle(LOCALIZATION_PREFIX + selectedLanguage);
            } catch (IllegalArgumentException e) {
                logger.warn("the language {} is not found", selectedLanguage, e);
                resourceBundle = ResourceBundle.getBundle(LOCALIZATION_PREFIX + DEFAULT_LOCALIZATION);
            }
        } else {
            resourceBundle = ResourceBundle.getBundle(LOCALIZATION_PREFIX + DEFAULT_LOCALIZATION);
        }
        return resourceBundle;
    }
}
