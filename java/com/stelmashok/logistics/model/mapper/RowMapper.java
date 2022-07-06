package com.stelmashok.logistics.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
// the method must create and store the values of the object from the ResultSet
public interface RowMapper<T> {
    Optional<T> mapRow(ResultSet resultSet) throws SQLException;
}
