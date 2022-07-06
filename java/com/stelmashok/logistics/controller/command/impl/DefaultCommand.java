package com.stelmashok.logistics.controller.command.impl;

import com.stelmashok.logistics.controller.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        return new Router(ERROR_PAGE_404, ERROR);
    }
}
