package com.stelmashok.logistics.model.mapper.impl;

import com.stelmashok.logistics.model.entity.Product;
import com.stelmashok.logistics.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.stelmashok.logistics.model.mapper.DatabaseColumnTitleHolder.*;

public class ProductRowMapper implements RowMapper<Product> {
    private static final Logger logger = LogManager.getLogger();
    // the method create an object and store the values from the resultSet in the object
    @Override
    public Optional<Product> mapRow(ResultSet resultSet) throws SQLException {
        try {
            Product product = new Product();
            product.setId(resultSet.getLong(PRODUCT_ID));
            product.setTitle(resultSet.getString(PRODUCT_TITLE));
            product.setWeight(resultSet.getInt(PRODUCT_WEIGHT));
            product.setDescription(resultSet.getString(PRODUCT_DESCRIPTION));
            return Optional.of(product); // return an object with non-null content
        } catch (SQLException e) {
            logger.error("failed to fetch products rows", e);
            return Optional.empty();
        }
    }
}
