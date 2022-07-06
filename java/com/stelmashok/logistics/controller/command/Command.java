package com.stelmashok.logistics.controller.command;

import jakarta.servlet.http.HttpServletRequest;

public interface Command {

    Router execute(HttpServletRequest request);

    default long parseLongParameter(String input) {
        try {
            return Long.parseLong((input));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    default int parseIntParameter(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
