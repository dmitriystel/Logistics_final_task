package com.stelmashok.logistics.controller.command.impl.post;

import com.stelmashok.logistics.controller.command.Command;
import com.stelmashok.logistics.controller.command.Router;
import com.stelmashok.logistics.service.UserService;
import com.stelmashok.logistics.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ActivateUserCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String activationToken = request.getParameter(TOKEN);
        UserService userService = UserServiceImpl.getInstance();

        boolean activationResult = userService.activateUserByHash(activationToken);
        if (activationResult) {
            session.setAttribute(ACTIVATION_FEEDBACK, MESSAGE_SUCCESS_ACTIVATION);
        } else {
            session.setAttribute(ACTIVATION_FEEDBACK, MESSAGE_FAIL_ACTIVATION);
        }
        return new Router(ACTIVATE_USER_PAGE, FORWARD);
    }
}
