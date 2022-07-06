package com.stelmashok.logistics.model.mapper.impl;

import com.stelmashok.logistics.model.entity.*;
import com.stelmashok.logistics.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.stelmashok.logistics.model.mapper.DatabaseColumnTitleHolder.*;

public class TransportOrderRowMapper implements RowMapper<TransportOrder> {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Optional<TransportOrder> mapRow(ResultSet resultSet) throws SQLException {
        try {
            TransportOrder transportOrder = new TransportOrder();

            transportOrder.setId(resultSet.getLong(TRANSPORT_ORDER_ID));

            UserRowMapper userOrderRowMapper = new UserRowMapper();
            Optional<User> user = userOrderRowMapper.mapRow(resultSet);
            user.ifPresent(transportOrder::setUser);    //  ?

            transportOrder.setOrderDate(resultSet.getObject(TRANSPORT_ORDER_DATE, LocalDateTime.class));
            transportOrder.setDeliveryDate(resultSet.getObject(TRANSPORT_ORDER_DELIVERY_DATE, LocalDateTime.class));

            ProductRowMapper productRowMapper = new ProductRowMapper();
            Optional<Product> product = productRowMapper.mapRow(resultSet);
            product.ifPresent(transportOrder::setProduct);

            UnloadingRowMapper unloadingRowMapper = new UnloadingRowMapper();
            Optional<Unloading> unloading = unloadingRowMapper.mapRow(resultSet);
            unloading.ifPresent(transportOrder::setUnloading);

            CarrierRowMapper carrierRowMapper = new CarrierRowMapper();
            Optional<Carrier> carrier = carrierRowMapper.mapRow(resultSet);
            carrier.ifPresent(transportOrder::setCarrier);

            return Optional.of(transportOrder);
        } catch (SQLException e) {
            logger.error("failed to fetch transport orders rows", e);
            return Optional.empty();
        }
    }
}
