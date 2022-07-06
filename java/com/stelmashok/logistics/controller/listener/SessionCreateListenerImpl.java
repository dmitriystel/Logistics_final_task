package com.stelmashok.logistics.controller.listener;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

@WebListener
public class SessionCreateListenerImpl implements HttpSessionListener {
    static Logger logger = (Logger) LogManager.getLogger();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.log(Level.INFO, "---------------> sessionCreated: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.log(Level.INFO, "---------------> sessionDestroyed: " + se.getSession().getId());
    }


}
