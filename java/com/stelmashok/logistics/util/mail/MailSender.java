package com.stelmashok.logistics.util.mail;


import com.mysql.cj.protocol.Message;   //  или выбирать гугл

import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import com.sun.jdi.connect.Transport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailSender {

    private static final String USER_NAME = "mail.smtp.user";
    private static final Properties properties;
    private static final Logger logger = LogManager.getLogger();
    private static final String PROPERTIES_FILE = "config/mail.properties";
    private static MailSender instance;

    static {
        properties = new Properties();
        try (InputStream inputStream = MailSender.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            logger.error("failed to find mail properties file");
        } catch (IOException e) {
            logger.error("failed to read mail properties file");
        }
    }

    private MailSender() {
    }

    public static MailSender getInstance() {
        if (instance == null) {
            instance = new MailSender();
        }
        return instance;
    }

    public void sendMail(String recipientMail, String subject, String text) {
        try {
            Session session = SessionFactory.createSession(properties);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty(USER_NAME)));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientMail));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            logger.info("message has been send successfully");

        } catch (MessagingException e) {
            logger.error("failed to send the message", e);
        }
    }
}
