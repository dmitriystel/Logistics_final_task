package com.stelmashok.logistics.model.mapper.impl;

import com.stelmashok.logistics.model.entity.Carrier;
import com.stelmashok.logistics.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.stelmashok.logistics.model.mapper.DatabaseColumnTitleHolder.*;

public class CarrierRowMapper implements RowMapper<Carrier> {

    private static final Logger logger = LogManager.getLogger();
    // the method create an object and store the values from the resultSet in the object
    @Override
    public Optional<Carrier> mapRow(ResultSet resultSet) throws SQLException {
        try {
            Carrier carrier = new Carrier();
            carrier.setId(resultSet.getLong(CARRIER_ID));
            carrier.setCarrierName(resultSet.getString(CARRIER_NAME));
            carrier.setTruckNumber(resultSet.getString(CARRIER_TRUCK_NUMBER));
            return Optional.of(carrier); // return an object with non-null content
        } catch (SQLException e) {
            logger.error("failed to fetch carriers rows", e);
            return Optional.empty(); // return an empty object
        }
    }
}
