package com.stelmashok.logistics.model.mapper.impl;

import com.stelmashok.logistics.model.entity.User;
import com.stelmashok.logistics.model.entity.UserRole;
import com.stelmashok.logistics.model.entity.UserStatus;
import com.stelmashok.logistics.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;

import static com.stelmashok.logistics.model.mapper.DatabaseColumnTitleHolder.*;

public class UserRowMapper implements RowMapper<User> {
    private static final Logger logger = LogManager.getLogger();
    // the method create an object and store the values from the resultSet in the object
    @Override
    public Optional<User> mapRow(ResultSet resultSet) throws SQLException {
        try {
            User user = new User();
            user.setId(resultSet.getLong(USER_ID));
            user.setCustomerName(resultSet.getString(USER_CUSTOMER_NAME));
            user.setLogin(resultSet.getString(USER_LOGIN));
            user.setEmail(resultSet.getString(USER_EMAIL));
            user.setName(resultSet.getString(USER_NAME));
            user.setSurname(resultSet.getString(USER_SURNAME));
            user.setPhone(resultSet.getString(USER_PHONE));
            String status = resultSet.getString(USER_STATUS).toUpperCase();
            String role = resultSet.getString(USER_ROLE).toUpperCase(Locale.ROOT);
            user.setStatus(UserStatus.valueOf(status));
            user.setRole(UserRole.valueOf(role)); // return an object with non-null content
            return Optional.of(user);
        } catch (SQLException e) {
            logger.error("failed to fetch data from the result set");
            return Optional.empty();
        }
    }
}
