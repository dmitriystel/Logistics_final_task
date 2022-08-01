package com.stelmashok.logistics.util.mail;

import com.mysql.cj.xdevapi.Session;

import java.net.PasswordAuthentication;
import java.util.Properties;

public class SessionFactory {

    static final String USER_NAME = "mail.smtp.user";
    static final String USER_PASSWORD = "mail.smtp.password";

    private SessionFactory() {
    }

    static Session createSession(Properties configProperties) {
        String userName = configProperties.getProperty(USER_NAME);
        String userPassword = configProperties.getProperty(USER_PASSWORD);
        return Session.getInstance(configProperties,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, userPassword);
                    }
                });
    }
}
