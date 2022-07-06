package com.stelmashok.logistics.model.mapper.impl;

import com.stelmashok.logistics.model.entity.Unloading;
import com.stelmashok.logistics.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.stelmashok.logistics.model.mapper.DatabaseColumnTitleHolder.*;

public class UnloadingRowMapper implements RowMapper<Unloading> {
    private static final Logger logger = LogManager.getLogger();
    // the method create an object and store the values from the resultSet in the object
    @Override
    public Optional<Unloading> mapRow(ResultSet resultSet) throws SQLException {
        try {
            Unloading unloading = new Unloading();
            unloading.setId(resultSet.getLong(UNLOADING_ID));
            unloading.setCountry(resultSet.getString(UNLOADING_COUNTRY));
            unloading.setCity(resultSet.getString(UNLOADING_CITY));
            return Optional.of(unloading); // return an object with non-null content
        } catch (SQLException e) {
            logger.error("failed to fetch unloadings rows", e);
            return Optional.empty();
        }
    }
}
