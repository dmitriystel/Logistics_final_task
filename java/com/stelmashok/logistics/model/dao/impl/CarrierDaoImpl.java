package com.stelmashok.logistics.model.dao.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.CarrierDao;
import com.stelmashok.logistics.model.dao.CustomJdbcTemplate;
import com.stelmashok.logistics.model.entity.Carrier;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.model.mapper.impl.CarrierRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
//     private static final String SQL_PAGINATION = " LIMIT ?, ? "; // сколько показывать, откуда стартовать
// findById - этого метода нет в сервисах. Подумать, возможно не нужен поиск перевозчика по id
// public boolean delete(Carrier carrier) throws DaoException - есть тут, нет в сервисе. Дописать тут?
public class CarrierDaoImpl extends AbstractDao<Carrier> implements CarrierDao {

    private static final Logger logger = LogManager.getLogger();

    private static final String SQL_CREATE_CARRIER = """
            INSERT INTO carriers(carrier_name, truck_number)
            VALUES(?, ?)""";

    private static final String SQL_IS_CARRIER_EXIST = "SELECT carrier_id FROM carriers WHERE carrier_name = ? LIMIT 1";
    private static final String SQL_IS_TRUCK_NUMBER_EXIST = "SELECT carrier_id FROM carriers WHERE truck_number = ? LIMIT 1";

    private static final String SQL_FIND_ALL_CARRIERS = """
            SELECT carrier_id, carrier_name
            FROM carriers
            ORDER BY id ASC""";

    private static final String SQL_FIND_BY_ID = """
            SELECT carrier_id, carrier_name
            FROM carriers
            WHERE carrier_id = ?""";

    private static final String SQL_FIND_BY_CARRIER_NAME = """
            SELECT carrier_id, carrier_name
            FROM carriers
            WHERE carrier_name = ?""";

    private static final String SQL_UPDATE_CARRIER = """
            UPDATE carriers
            SET carrier_name = ?
            WHERE carrier_id = ?""";

    private static final String SQL_DELETE_CARRIER = """
            DELETE FROM carriers
            WHERE carriers.carrier_id = ?""";

    private static final String SQL_COUNT_ALL_CARRIERS = "SELECT COUNT(carriers.carrier_id) FROM carriers";

    // ? - how many values to show, ? - from what value to show
    private static final String SQL_PAGINATION = " LIMIT ?, ? ";
    // class implements queries and updates
    private final CustomJdbcTemplate<Carrier> customJdbcTemplate = new CustomJdbcTemplate<>();
    // mapRow method fills an object with data from resultSet
    private final CarrierRowMapper carrierRowMapper = new CarrierRowMapper();

    @Override
    public boolean create(Carrier carrier) throws DaoException {
        Object[] args = {carrier.getCarrierName(), carrier.getTruckNumber()};
        return customJdbcTemplate.update(connection, SQL_CREATE_CARRIER, args) >= 0;
    }

    @Override
    public boolean isExistCarrier(String carrierName) throws DaoException {
        // creating an object to send requests
        try (PreparedStatement statement = connection.prepareStatement(SQL_IS_CARRIER_EXIST)) {
            statement.setString(1, carrierName); // set value
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("failed to check if carrier with {} exists", carrierName, e);
            throw new DaoException("failed to check if carrier with" + carrierName + " exists", e);
        }
    }

    @Override
    public boolean isExistTruck(String truckNumber) throws DaoException {
        // creating an object to send requests
        try (PreparedStatement statement = connection.prepareStatement(SQL_IS_TRUCK_NUMBER_EXIST)) {
            statement.setString(1, truckNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("failed to check if truck with {} exists", truckNumber, e);
            throw new DaoException("failed to check if truck with" + truckNumber + " exists", e);
        }
    }

    @Override
    public List<Carrier> findAllCarriers() throws DaoException {
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_CARRIERS, carrierRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find carriers", e);
            throw new DaoException("failed to find carriers", e);
        }
    }

    @Override
    public List<Carrier> findAllPaginatedCarriers(int currentPage, int carriersPerPage) throws DaoException {
        int startItem = currentPage * carriersPerPage - carriersPerPage;
        Object[] args = {startItem, carriersPerPage};
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_CARRIERS + SQL_PAGINATION, args, carrierRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find carriers", e);
            throw new DaoException("failed to find carriers", e);
        }
    }

    @Override
    public Optional<Carrier> findById(long id) throws DaoException {
        Object[] args = {id};
        List<Carrier> carriers;
        try {
            carriers = customJdbcTemplate.query(connection, SQL_FIND_BY_ID, args, carrierRowMapper);
            if (!carriers.isEmpty()) {
                return Optional.ofNullable(carriers.get(0)); // creates an object where content may be null
            }
        } catch (DaoException e) {
            logger.error("failed to find carrier", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Carrier> findByCarrierName(String carrierName) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_CARRIER_NAME)) {  // creating an object to send requests
            statement.setString(1, carrierName);
            try (ResultSet resultSet = statement.executeQuery()) { // execution of a request
                if (resultSet.next()) { // handling the results of a data sampling query / contains data
                    return carrierRowMapper.mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("failed to find any carrier by name", e);
            throw new DaoException("failed to find any carrier by name", e);
        }
        return Optional.empty();
    }

    @Override
    public int countAllCarriers() throws DaoException {
        return customJdbcTemplate.query(connection, SQL_COUNT_ALL_CARRIERS);
    }

    @Override
    public Optional<Carrier> update(Carrier carrier) throws DaoException {
        Object[] args = {carrier.getCarrierName(), carrier.getTruckNumber(), carrier.getId()};
        return customJdbcTemplate.update(connection, SQL_UPDATE_CARRIER, args, carrier);
    }

    @Override
    public boolean delete(Carrier carrier) throws DaoException {
        return delete(carrier.getId());
    }
    // ok service - dao
    @Override
    public boolean delete(long id) throws DaoException {
        Object[] args = {id};
        try {
            return customJdbcTemplate.update(connection, SQL_DELETE_CARRIER, args) >= 0;
        } catch (DaoException e) {
            logger.error("failed to delete carrier with id {}", id, e);
            throw new DaoException("failed to delete carrier", e);
        }
    }
}
