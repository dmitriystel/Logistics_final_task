package com.stelmashok.logistics.controller.command.impl.post;

import com.stelmashok.logistics.controller.command.Command;
import com.stelmashok.logistics.controller.command.Router;
import jakarta.servlet.http.HttpServletRequest;

public class AddTransportOrderCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        return null;
    }
}
