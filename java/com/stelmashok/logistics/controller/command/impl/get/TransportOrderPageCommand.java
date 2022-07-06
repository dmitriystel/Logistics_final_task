package com.stelmashok.logistics.controller.command.impl.get;

import com.stelmashok.logistics.controller.command.Command;
import com.stelmashok.logistics.controller.command.Router;
import jakarta.servlet.http.HttpServletRequest;

public class TransportOrderPageCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        return null;
    }
}
