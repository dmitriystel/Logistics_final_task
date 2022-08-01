package com.stelmashok.logistics.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum CommandTypeHolder {

    DEFAULT_COMMAND,
    HOME_PAGE,
    CHANGE_LANGUAGE,
    SIGN_IN,
    SIGN_UP_PAGE,
    LOG_OUT,
    SIGN_UP,
    USER_MANAGEMENT_PAGE,
    CATEGORY_MANAGEMENT_PAGE,
    CITY_MANAGEMENT_PAGE,
    ITEM_PAGE,
    USER_PAGE,
    ADD_ITEM_PAGE,
    ADD_ITEM,
    EDIT_ITEM,
    DELETE_ITEM,
    EDIT_ITEM_PAGE,
    ADD_COMMENT,
    DELETE_COMMENT,
    BOOKMARK_PAGE,
    ADD_BOOKMARK,
    DELETE_BOOKMARK,
    ACTIVATE_USER,
    ADD_CITY,
    EDIT_CITY,
    DELETE_CITY,
    ADD_CATEGORY,
    EDIT_CATEGORY,
    DELETE_CATEGORY,
    EDIT_USER_PAGE,
    CHANGE_PASSWORD,
    EDIT_USER_PROFILE,
    SAVE_USER_CHANGES;

    private static final Logger logger = LogManager.getLogger();

    public static CommandTypeHolder getCommandType(String command) {
        if (command == null) {
            logger.error("the command is null");
            return DEFAULT_COMMAND;
        }
        CommandTypeHolder type;
        try {
            type = CommandTypeHolder.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("can't find the command", e);
            type = DEFAULT_COMMAND;
        }
        return type;
    }
}
