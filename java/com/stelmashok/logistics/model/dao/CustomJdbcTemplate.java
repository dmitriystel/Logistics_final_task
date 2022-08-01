package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomJdbcTemplate<T> {
    private static final Logger logger = LogManager.getLogger();

    public List<T> query(Connection connection, String sql, Object[] args, RowMapper<T> rowMapper) throws DaoException {
        List<T> resultList = new ArrayList<>(); // creating an ArrayList to store the result
        try (PreparedStatement statement = connection.prepareStatement(sql)) { // creating an object to send requests
            PreparedStatementSetter.setValues(statement, args); // setting values by index for preparedStatement object
            try (ResultSet resultSet = statement.executeQuery()) { // execution of a request
                while (resultSet.next()) { // handling the results of a data sampling query / contains data
                    Optional<T> entity = rowMapper.mapRow(resultSet); // create an object and set values from the resultset
                    resultList.add(entity.get()); // add values to the resultList
                }
            }
        } catch (SQLException e) {
            logger.error("failed to execute a query", e);
            throw new DaoException("failed to execute a query", e);
        }
        return resultList;
    }

    public int query(Connection connection, String sql) throws DaoException {
        int result = 0;
        try (Statement statement = connection.createStatement()) { // creating an object to send requests
            // The results of the selection from the database are placed in the ResultSet object
            try (ResultSet resultSet = statement.executeQuery(sql)) { // execution of a request
                if (resultSet.next()) { // handling the results of a data sampling query / contains data
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("failed to execute a query", e);
            throw new DaoException("failed to execute a query", e);
        }
        return result;
    }

    public int query(Connection connection, String sql, Object[] args) throws DaoException {
        int result = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql)) { // creating an object to send requests
            PreparedStatementSetter.setValues(statement, args); // setting values for preparedStatement object
            try (ResultSet resultSet = statement.executeQuery()) { // execution of a request
                if (resultSet.next()) { // handling the results of a data sampling query / contains data
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("failed to execute a query", e);
            throw new DaoException("failed to execute a query", e);
        }
        return result;
    }

    public List<T> query(Connection connection, String sql, RowMapper<T> rowMapper) throws DaoException {
        List<T> resultList = new ArrayList<>(); // creating an ArrayList to store the result
        try (Statement statement = connection.createStatement()) { // creating an object to send requests
            // The results of the selection from the database are put in the ResultSet object
            try (ResultSet resultSet = statement.executeQuery(sql)) { // execution of a request
                while (resultSet.next()) { // handling the results of a data sampling query / contains data
                    Optional<T> entity = rowMapper.mapRow(resultSet); // create an object and set values from the resultset
                    resultList.add(entity.get()); // add values to the resultList
                }
            }
        } catch (SQLException e) {
            logger.error("failed to execute a query", e);
            throw new DaoException("failed to execute a query", e);
        }
        return resultList;
    }

    public int update(Connection connection, String sql, Object[] args) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) { // creating an object to send requests
            PreparedStatementSetter.setValues(statement, args); // setting values for preparedStatement object
            return statement.executeUpdate(); // execution of a request
        } catch (SQLException e) {
            logger.error("failed to execute an update", e);
            throw new DaoException("failed to execute an update", e);
        }
    }

    public Optional<T> update(Connection connection, String sql, Object[] args, T t) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) { // creating an object to send requests
            PreparedStatementSetter.setValues(statement, args); // setting values for preparedStatement object
            if (statement.executeUpdate() >= 0) { // execution of a request
                return Optional.of(t); // creates an object with non-null content
            }
        } catch (SQLException e) {
            logger.error("failed to execute an update", e);
            throw new DaoException("failed to execute an update", e);
        }
        return Optional.empty();
    }
}
