package com.stelmashok.logistics.model.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
// establishing a connection to the database
public class ConnectionFactory {

    private static final Logger logger = LogManager.getLogger();

    private static final Properties properties = new Properties(); // to store lists of values where the key is a string
    private static final String DATABASE_PROPERTIES_PATH = "config/database.properties";
    private static final String DATABASE_DRIVER_PROPERTY = "driver"; // com.mysql.cj.jdbc.Driver
    private static final String DATABASE_USER_PROPERTY = "user"; // admin
    private static final String DATABASE_PASSWORD_PROPERTY = "password"; // 321 - show for educational project only
    private static final String DATABASE_URL_PROPERTY = "url"; // jdbc:mysql://localhost:3306/logistics
    private static final String DATABASE_DRIVER;
    private static final String DATABASE_USER;
    private static final String DATABASE_PASSWORD;
    private static final String DATABASE_URL;

    static {    //  ? почему статический блок? Для инициализации один раз
        try (InputStream inputStream = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream(DATABASE_PROPERTIES_PATH)) {
            properties.load(inputStream); // load all the information into properties object from database.properties

            // assign a value to each constant - String getProperty(String key)
            DATABASE_URL = properties.getProperty(DATABASE_URL_PROPERTY); //  jdbc:mysql://localhost:3306/logistics
            DATABASE_USER = properties.getProperty(DATABASE_USER_PROPERTY); //  admin
            DATABASE_PASSWORD = properties.getProperty(DATABASE_PASSWORD_PROPERTY); //  321
            DATABASE_DRIVER = properties.getProperty(DATABASE_DRIVER_PROPERTY); //  com.mysql.cj.jdbc.Driver
            // standard driver registration or DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver())
            Class.forName(DATABASE_DRIVER);
        } catch (IOException e) {
            logger.error("failed to read database properties", e);
            throw new RuntimeException("failed to read database properties", e);
        } catch (ClassNotFoundException e) {
            logger.error("driver was not found");
            throw new RuntimeException("driver was not found");
        }
    }

    private ConnectionFactory() {
    }
    // establishing a database connection using the method getConnection(), parameters - url, login, password
    static ProxyConnection createConnection() throws SQLException {
        return new ProxyConnection(DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD));
    }
}
