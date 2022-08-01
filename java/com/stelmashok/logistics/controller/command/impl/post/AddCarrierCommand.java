package com.stelmashok.logistics.controller.command.impl.post;

import com.stelmashok.logistics.controller.command.Command;
import com.stelmashok.logistics.controller.command.Router;
import com.stelmashok.logistics.model.entity.User;
import com.stelmashok.logistics.service.CarrierService;
import com.stelmashok.logistics.service.impl.CarrierServiceImpl;
import com.stelmashok.logistics.util.validator.FormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AddCarrierCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        CarrierService carrierService = CarrierServiceImpl.getInstance();

        // написать валидатор
        FormValidator validator = AddCarrierFormValidator.getInstance();

        return null;
    }
}
