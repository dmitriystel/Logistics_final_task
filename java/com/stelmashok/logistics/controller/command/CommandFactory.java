package com.stelmashok.logistics.controller.command;

import com.stelmashok.logistics.controller.command.impl.DefaultCommand;
import com.stelmashok.logistics.controller.command.impl.get.HomePageCommand;
import com.stelmashok.logistics.controller.command.impl.get.UserManagementPageCommand;
import com.stelmashok.logistics.controller.command.impl.get.UserPageCommand;
import com.stelmashok.logistics.controller.command.impl.post.*;

import java.util.EnumMap;

public final class CommandFactory {
    private static final CommandFactory instance = new CommandFactory();
    private final EnumMap<CommandTypeHolder, Command> commands;

    private CommandFactory() {
        commands = new EnumMap<>(CommandTypeHolder.class);
        commands.put(CommandTypeHolder.DEFAULT_COMMAND, new DefaultCommand());
        commands.put(CommandTypeHolder.HOME_PAGE, new HomePageCommand());
        commands.put(CommandTypeHolder.CHANGE_LANGUAGE, new ChangeLanguageCommand());
        commands.put(CommandTypeHolder.SIGN_IN, new SignInCommand());
        commands.put(CommandTypeHolder.SIGN_UP_PAGE, new SignUpPageCommand());
        commands.put(CommandTypeHolder.LOG_OUT, new LogOutCommand());
        commands.put(CommandTypeHolder.SIGN_UP, new SignUpCommand());
        commands.put(CommandTypeHolder.USER_MANAGEMENT_PAGE, new UserManagementPageCommand());
        commands.put(CommandTypeHolder.CATEGORY_MANAGEMENT_PAGE, new CategoryManagementPageCommand());
        commands.put(CommandTypeHolder.CITY_MANAGEMENT_PAGE, new CityManagementPageCommand());
        commands.put(CommandTypeHolder.ITEM_PAGE, new ItemPageCommand());
        commands.put(CommandTypeHolder.USER_PAGE, new UserPageCommand());
        commands.put(CommandTypeHolder.ADD_COMMENT, new AddCommentCommand());
        commands.put(CommandTypeHolder.DELETE_COMMENT, new DeleteCommentCommand());
        commands.put(CommandTypeHolder.ADD_ITEM, new AddItemCommand());
        commands.put(CommandTypeHolder.ADD_ITEM_PAGE, new AddItemPageCommand());
        commands.put(CommandTypeHolder.EDIT_ITEM_PAGE, new EditItemPageCommand());
        commands.put(CommandTypeHolder.EDIT_ITEM, new EditItemCommand());
        commands.put(CommandTypeHolder.DELETE_ITEM, new DeleteItemCommand());
        commands.put(CommandTypeHolder.BOOKMARK_PAGE, new BookmarkPageCommand());
        commands.put(CommandTypeHolder.ADD_BOOKMARK, new AddBookmarkCommand());
        commands.put(CommandTypeHolder.DELETE_BOOKMARK, new DeleteBookmarkCommand());
        commands.put(CommandTypeHolder.ACTIVATE_USER, new ActivateUserCommand());
        commands.put(CommandTypeHolder.ADD_CITY, new AddCityCommand());
        commands.put(CommandTypeHolder.EDIT_CITY, new EditCityCommand());
        commands.put(CommandTypeHolder.DELETE_CITY, new DeleteCityCommand());
        commands.put(CommandTypeHolder.ADD_CATEGORY, new AddCategoryCommand());
        commands.put(CommandTypeHolder.EDIT_CATEGORY, new EditCategoryCommand());
        commands.put(CommandTypeHolder.DELETE_CATEGORY, new DeleteCategoryCommand());
        commands.put(CommandTypeHolder.EDIT_USER_PAGE, new EditUserPageCommand());
        commands.put(CommandTypeHolder.CHANGE_PASSWORD, new ChangePasswordCommand());
        commands.put(CommandTypeHolder.EDIT_USER_PROFILE, new EditUserProfileCommand());
        commands.put(CommandTypeHolder.SAVE_USER_CHANGES, new SaveUserChangesCommand());
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    public Command getCommand(String command) {
        CommandTypeHolder commandTypeHolder = CommandTypeHolder.getCommandType(command);
        return commands.get(commandTypeHolder);
    }
}
