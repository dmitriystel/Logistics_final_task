package com.stelmashok.logistics.model.dao.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.CustomJdbcTemplate;
import com.stelmashok.logistics.model.dao.TransportOrderDao;
import com.stelmashok.logistics.model.entity.*;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.model.mapper.impl.TransportOrderRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TransportOrderDaoImpl extends AbstractDao<TransportOrder> implements TransportOrderDao {

    private static final Logger logger = LogManager.getLogger();

    private static final String SQL_CREATE_ORDER = """
            INSERT INTO transport_orders(user_id, order_date, delivery_date, product_id, unloading_id, carrier_id)
            VALUES(?, ?, ?, ?, ?, ?)""";

    private static final String SQL_FIND_ALL_ORDERS = """
            SELECT order_id
            FROM transport_orders
            ORDER BY id ASC""";

    private static final String SQL_FIND_ORDER_BY_ID = """
            SELECT transport_orders.order_id, transport_orders.order_date, transport_orders.delivery_date, 
                   users.customer_name, users.email, users.name, users.surname, users.phone, user_statuses.title,
                   user_roles.title, products.title, products.weight, products.description, unloadings.country, 
                   unloadings.city, carriers.carrier_name, carriers.truck_number
            FROM transport_orders
            JOIN users ON transport_orders.user_id = users.user_id
            JOIN user_statuses ON users.user_statuses_id = user_statuses.user_statuses_id
            JOIN user_roles ON users.user_roles_id = user_roles.user_roles_id
            JOIN products ON transport_orders.product_id = products.product_id
            JOIN unloadings ON transport_orders.unloading_id = unloadings.unloading_id
            JOIN carriers NO transport_orders.carrier_id = carriers.carrier_id
            WHERE transport_orders.order_id = ?""";

    //   проверить верен ли запрос, возможно ошибка в where
    private static final String SQL_FIND_ORDER_BY_CUSTOMER_NAME = """
            SELECT transport_orders.order_id, transport_orders.order_date, transport_orders.delivery_date, 
                   users.customer_name, users.email, users.name, users.surname, users.phone, user_statuses.title,
                   user_roles.title, products.title, products.weight, products.description, unloadings.country, 
                   unloadings.city, carriers.carrier_name, carriers.truck_number
            FROM transport_orders
            JOIN users ON transport_orders.user_id = users.user_id
            JOIN user_statuses ON users.user_statuses_id = user_statuses.user_statuses_id
            JOIN user_roles ON users.user_roles_id = user_roles.user_roles_id
            JOIN products ON transport_orders.product_id = products.product_id
            JOIN unloadings ON transport_orders.unloading_id = unloadings.unloading_id
            JOIN carriers NO transport_orders.carrier_id = carriers.carrier_id
            WHERE users.customer_name = ?""";


    private static final String SQL_UPDATE_ORDER = """
            UPDATE transport_orders
            SET order_date = ?, delivery_date = ?, product_id = ?, unloading_id = ?, carrier_id = ?
            WHERE transport_orders.order_id = ?""";

    private static final String SQL_DELETE_ORDER_BY_ID = """
            DELETE FROM transport_orders
            WHERE transport_orders.id = ?""";


    private static final String SQL_COUNT_ORDERS = "SELECT COUNT(order_id) FROM transport_orders";

    private static final String SQL_IS_ORDER_EXIST = "SELECT order_id FROM transport_orders WHERE order_date = ? LIMIT 1";
    //  что это?
    private static final String SQL_PAGINATION = " LIMIT ?, ? ";

    private final TransportOrderRowMapper transportOrderRowMapper = new TransportOrderRowMapper();
    private final CustomJdbcTemplate<TransportOrder> customJdbcTemplate = new CustomJdbcTemplate<>();

    @Override
    public boolean create(TransportOrder transportOrder) throws DaoException {
        Object[] args = {
                transportOrder.getUser().getId(),
                transportOrder.getOrderDate(),
                transportOrder.getDeliveryDate(),
                transportOrder.getProduct().getId(),
                transportOrder.getUnloading().getId(),
                transportOrder.getCarrier().getId()};
        return customJdbcTemplate.update(connection, SQL_CREATE_ORDER, args) >= 0;
    }

    @Override
    public boolean isExistTransportOrder(LocalDateTime orderDate) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_IS_ORDER_EXIST)) {
            statement.setString(1, String.valueOf(orderDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("failed to check if transport order exist", orderDate, e);
            throw new DaoException("failed to check if transport order with " + orderDate + " exists", e);
        }
    }

    @Override
    public List<TransportOrder> findAllTransportOrders() throws DaoException {
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_ORDERS, transportOrderRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find transport orders", e);
            throw new DaoException("failed to find transport orders", e);
        }
    }

    @Override
    public List<TransportOrder> findAllPaginatedTransportOrders(int currentPage, int transportOrdersPerPage) throws DaoException {
        int startItem = currentPage * transportOrdersPerPage - transportOrdersPerPage;
        Object[] args = {startItem, transportOrdersPerPage};
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_ORDERS + SQL_PAGINATION, args, transportOrderRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find transport orders", e);
            throw new DaoException("failed to find transport orders", e);
        }
    }

    @Override
    public Optional<TransportOrder> findById(long id) throws DaoException {
        Object[] args = {id};
        List<TransportOrder> transportOrders;
        try {

            transportOrders = customJdbcTemplate.query(connection, SQL_FIND_ORDER_BY_ID, args, transportOrderRowMapper);
            if (!transportOrders.isEmpty()) {
                return Optional.ofNullable(transportOrders.get(0));
            }
        } catch (DaoException e) {
            logger.error("failed to find the transport order with id {}", id, e);
        }
        return Optional.empty();
    }

    // Как реализовать? Правильно ли?
    @Override
    public Optional<TransportOrder> findByCustomerName(String customerName) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ORDER_BY_CUSTOMER_NAME)) {
            statement.setString(1, customerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return transportOrderRowMapper.mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("failed to find any transport order by customer name", e);
            throw new DaoException("failed to find any transport order by customer name", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<TransportOrder> update(TransportOrder transportOrder) throws DaoException {
        Object[] args = {
                transportOrder.getUser().getId(),
                transportOrder.getOrderDate(),
                transportOrder.getDeliveryDate(),
                transportOrder.getProduct().getId(),
                transportOrder.getUnloading().getId(),
                transportOrder.getCarrier().getId(),
                transportOrder.getId()};
        return customJdbcTemplate.update(connection, SQL_UPDATE_ORDER, args, transportOrder);
    }

    @Override
    public boolean delete(TransportOrder transportOrder) throws DaoException {
        return delete(transportOrder.getId());
    }

    @Override
    public boolean delete(long id) throws DaoException {
        Object[] args = {id};
        try {
            return customJdbcTemplate.update(connection, SQL_DELETE_ORDER_BY_ID, args) >= 0;
        } catch (DaoException e) {
            logger.error("failed to delete any transport order with id {}", id, e);
            throw new DaoException("failed to delete any transport order", e);
        }
    }

    @Override
    public int countAllTransportOrders() throws DaoException {
        return customJdbcTemplate.query(connection, SQL_COUNT_ORDERS);
    }
}
