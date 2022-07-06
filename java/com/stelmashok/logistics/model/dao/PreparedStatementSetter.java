package com.stelmashok.logistics.model.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementSetter {
    public static void setValues(PreparedStatement preparedStatement, Object[] args) throws SQLException {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]); // установка значения по индексу
            }
        }
    }
}
