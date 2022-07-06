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

    // ?
    private static final String SQL_PAGINATION = " LIMIT ?, ? ";
    // class implements queries and updates
    private final CustomJdbcTemplate<Carrier> customJdbcTemplate = new CustomJdbcTemplate<>();
    // mapRow method fills an object with data from resultSet
    private final CarrierRowMapper carrierRowMapper = new CarrierRowMapper();

    // как работает метод? Откуда берутся данные в объекте carrier
    @Override
     // Carrier carrier - в параметры передается пустой объект или нет?
    public boolean create(Carrier carrier) throws DaoException {
        // создаем массив данных объекта carrier. Пустой, подготавливаем ячейки массива?
        Object[] args = {carrier.getCarrierName(), carrier.getTruckNumber()};
        return customJdbcTemplate.update(connection, SQL_CREATE_CARRIER, args) >= 0;
    }

    // ??? как работает метод?
    // что обозначает 1? Номер столбца после PK в таблице?
    @Override
    // в параметры передается имя перевозчика? Откуда?
    public boolean isExistCarrier(String carrierName) throws DaoException {
        // создаем объект для отправки запроса, передаем SQL запрос, без конкрытных параметров
        try (PreparedStatement statement = connection.prepareStatement(SQL_IS_CARRIER_EXIST)) { // creating an object to send requests
            // зачем добавляем информацию в табл? Название перевозчика должно быть добавлено, просто должны сравнить
            // или тут дабавляется название перевозчика в statement, а потом с помощью executeQuery() проверяем есть ли
            // такое значение в таблице?
            statement.setString(2, carrierName); // set value
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("failed to check if carrier with {} exists", carrierName, e);
            throw new DaoException("failed to check if carrier with" + carrierName + " exists", e);
        }
    }
    // ??? как работает метод? truckNumber - откуда берутся данные?
    // что обозначает 2? Номер столбца после PK в таблице?
    // как работает метод?
    @Override
    public boolean isExistTruck(String truckNumber) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_IS_TRUCK_NUMBER_EXIST)) { // creating an object to send requests
            statement.setString(3, truckNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("failed to check if truck with {} exists", truckNumber, e);
            throw new DaoException("failed to check if truck with" + truckNumber + " exists", e);
        }
    }
    // понятно как работает, данные извлекаются из таблицы (statement), присваиваются всем сущностям и возвращает список
    @Override
    public List<Carrier> findAllCarriers() throws DaoException {
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_CARRIERS, carrierRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find carriers", e);
            throw new DaoException("failed to find carriers", e);
        }
    }
    // разобраться что такое пагинация
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
    //   остановился тут
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

    //  1 ? верно?
    // как работает?
    @Override
    public Optional<Carrier> findByCarrierName(String carrierName) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_CARRIER_NAME)) {  // creating an object to send requests
            statement.setString(2, carrierName);
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
    public Optional<Carrier> update(Carrier carrier) throws DaoException {
        Object[] args = {carrier.getCarrierName(), carrier.getTruckNumber(), carrier.getId()};
        return customJdbcTemplate.update(connection, SQL_UPDATE_CARRIER, args, carrier);
    }

    @Override
    public boolean delete(Carrier carrier) throws DaoException {
        return delete(carrier.getId());
    }

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

    @Override
    public int countAllCarriers() throws DaoException {
        return customJdbcTemplate.query(connection, SQL_COUNT_ALL_CARRIERS);
    }
}
