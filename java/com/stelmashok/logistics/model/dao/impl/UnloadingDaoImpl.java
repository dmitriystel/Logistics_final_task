package com.stelmashok.logistics.model.dao.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.CustomJdbcTemplate;
import com.stelmashok.logistics.model.dao.UnloadingDao;
import com.stelmashok.logistics.model.entity.Unloading;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.model.mapper.impl.UnloadingRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UnloadingDaoImpl extends AbstractDao<Unloading> implements UnloadingDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String SQL_CREATE_UNLOADING = """
            INSERT INTO unloadings (country, city)
            VALUES(?, ?)""";

    private static final String SQL_IS_UNLOADING_EXIST = "SELECT unloading_id FROM unloadings WHERE city = ? LIMIT 1";

    private static final String SQL_FIND_ALL_UNLOADINGS = """
            SELECT unloading_id, country, city
            FROM unloadings
            ORDER BY id ASC""";

    private static final String SQL_FIND_UNLOADING_BY_ID = """
            SELECT unloading_id, country, city
            FROM unloadings
            WHERE unloading_id = ?""";

    private static final String SQL_FIND_UNLOADING_BY_COUNTRY = """
            SELECT unloading_id, country, city
            FROM unloadings
            WHERE country = ?""";

    private static final String SQL_FIND_UNLOADING_BY_CITY = """
            SELECT unloading_id, country, city
            FROM unloadings
            WHERE city = ?""";


    private static final String SQL_UPDATE_UNLOADING = """
            UPDATE unloadings SET country = ?, city = ?
            WHERE unloading_id = ?""";

    private static final String SQL_DELETE_UNLOADING_BY_ID = "DELETE FROM unloadings WHERE unloading_id = ?";



    // ?
    private static final String SQL_PAGINATION = " LIMIT ?, ? ";

    private final CustomJdbcTemplate<Unloading> customJdbcTemplate = new CustomJdbcTemplate<>();
    private final UnloadingRowMapper unloadingRowMapper = new UnloadingRowMapper();

    @Override
    public boolean create(Unloading unloading) throws DaoException {
        Object[] args = {unloading.getCountry(), unloading.getCity()};
        return customJdbcTemplate.update(connection, SQL_CREATE_UNLOADING, args) >= 0;
    }
    // что обозначает 2? Номер столбца после PK в таблице?
    @Override
    public boolean isExistUnloading(String city) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_IS_UNLOADING_EXIST)) {
            statement.setString(2, city);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("failed to check if unloading with {} exists", city, e);
            throw new DaoException("failed to check if unloading with" + city + " exists", e);
        }
    }

    @Override
    public List<Unloading> findAllUnloadings() throws DaoException {
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_UNLOADINGS, unloadingRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find unloadings", e);
            throw new DaoException("failed to find unloadings", e);
        }
    }

    @Override
    public List<Unloading> findAllPaginatedUnloadings(int currentPage, int unloadingsPerPage) throws DaoException {
        int startItem = currentPage * unloadingsPerPage - unloadingsPerPage;
        Object[] args = {startItem, unloadingsPerPage};
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_UNLOADINGS + SQL_PAGINATION, args, unloadingRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find unloadings", e);
            throw new DaoException("failed to find unloadings", e);
        }
    }


    @Override
    public Optional<Unloading> findById(long id) throws DaoException {
        Object[] args = {id};
        List<Unloading> unloadings;
        try {
            unloadings = customJdbcTemplate.query(connection, SQL_FIND_UNLOADING_BY_ID, args, unloadingRowMapper);
            if (!unloadings.isEmpty()) {
                return Optional.ofNullable(unloadings.get(0));
            }
        } catch (DaoException e) {
            logger.error("failed to find any unloading", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Unloading> findByCountry(String country) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_UNLOADING_BY_COUNTRY)) {
            statement.setString(2, country);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return unloadingRowMapper.mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("failed to find any unloading by country", e);
            throw new DaoException("failed to find any unloading by country", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Unloading> findByCity(String city) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_UNLOADING_BY_CITY)) {
            statement.setString(3, city);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return unloadingRowMapper.mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("failed to find any unloading by city", e);
            throw new DaoException("failed to find any unloading by city", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Unloading> update(Unloading unloading) throws DaoException {
        Object[] args = {unloading.getCountry(), unloading.getCity(), unloading.getId()};
        return customJdbcTemplate.update(connection, SQL_UPDATE_UNLOADING, args, unloading);
    }

    @Override
    public boolean delete(Unloading unloading) throws DaoException {
        return delete(unloading.getId());
    }

    @Override
    public boolean delete(long id) throws DaoException {
        Object[] args = {id};
        try {
            return customJdbcTemplate.update(connection, SQL_DELETE_UNLOADING_BY_ID, args) >= 0;
        } catch (DaoException e) {
            logger.error("failed to delete unloading with id {}", id, e);
            throw new DaoException("failed to delete item", e);
        }
    }

    @Override
    public int countAllUnloadings() throws DaoException {
        return 0;
    }
}
