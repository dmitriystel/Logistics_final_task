package com.stelmashok.logistics.model.dao.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.CustomJdbcTemplate;
import com.stelmashok.logistics.model.dao.ProductDao;
import com.stelmashok.logistics.model.entity.Product;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.model.mapper.impl.ProductRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl extends AbstractDao<Product> implements ProductDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String SQL_CREATE_PRODUCT = """
            INSERT INTO products(title, weight, description)
            VALUES(?, ?, ?)""";

    private static final String SQL_IS_PRODUCT_EXIST = "SELECT product_id FROM products WHERE title = ? LIMIT 1";

    private static final String SQL_FIND_ALL_PRODUCTS = """
            SELECT product_id, title, weight, description
            FROM products
            ORDER BY product_id ASC""";

    private static final String SQL_FIND_PRODUCT_BY_ID = """
            SELECT product_id, title, weight, description
            FROM products
            WHERE product_id = ?""";

    private static final String SQL_FIND_BY_TITLE = """
            SELECT product_id, title, weight, description
            FROM products
            WHERE title = ?""";

    private static final String SQL_UPDATE_PRODUCT = """
            UPDATE products
            SET products.title = ?, products.weight = ?, products.description = ?
            WHERE product_id = ?""";

    private static final String SQL_DELETE_PRODUCT_BY_ID = """
            DELETE FROM products
            WHERE product_id = ?""";
    // Для чего? Правильно ли?
    private static final String SQL_PAGINATION = " LIMIT ?, ? ";

    private static final String SQL_COUNT_PRODUCTS = "SELECT COUNT(products.product_id) FROM products";

    private final CustomJdbcTemplate<Product> customJdbcTemplate = new CustomJdbcTemplate<>();
    private final ProductRowMapper productRowMapper = new ProductRowMapper();

    @Override
    public boolean create(Product product) throws DaoException {
        Object[] args = {
                product.getTitle(),
                product.getWeight(),
                product.getDescription()};
        return customJdbcTemplate.update(connection, SQL_CREATE_PRODUCT, args) >= 0;
    }
// как работает?
    @Override
    public boolean isExistProduct(String title) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_IS_PRODUCT_EXIST)) {  // creating an object to send requests
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) { // execution of a request
                return resultSet.next(); // handling the results of a data sampling query / contains data
            }
        } catch (SQLException e) {
            logger.error("failed to check if title of product with {} exists", title, e);
            throw new DaoException("failed to check if product with " + title + " exist", e);
        }
    }

    @Override
    public List<Product> findAllProducts() throws DaoException {
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_PRODUCTS, productRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find products", e);
            throw new DaoException("failed to find products", e);
        }
    }

    @Override
    public List<Product> findAllPaginatedProducts(int currentPage, int productsPerPage) throws DaoException {
        int startItem = currentPage * productsPerPage - productsPerPage;
        Object[] args = {startItem, productsPerPage};
        try {
            return customJdbcTemplate.query(connection, SQL_FIND_ALL_PRODUCTS + SQL_PAGINATION, args, productRowMapper);
        } catch (DaoException e) {
            logger.error("failed to find products", e);
            throw new DaoException("failed to find products", e);
        }
    }

    @Override
    public Optional<Product> findById(long id) throws DaoException {
        Object[] args = {id};
        List<Product> products;
        try {
            products = customJdbcTemplate.query(connection, SQL_FIND_PRODUCT_BY_ID, args, productRowMapper);
            if (!products.isEmpty()) {
                // <T> Optional<T> ofNullable(T value) — создает объект, содержимое которого может быть нулевым.
                return Optional.ofNullable(products.get(0));
            }
        } catch (DaoException e) {
            logger.error("failed to find a product with id {}", id, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findByTitle(String title) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_TITLE)) {  // creating an object to send requests
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) { // execution of a request
                if (resultSet.next()) { // handling the results of a data sampling query / contains data
                    return productRowMapper.mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("failed to find any product by title", e);
            throw new DaoException("failed to find any product by title", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> update(Product product) throws DaoException {
        Object[] args = {product.getTitle(), product.getWeight(), product.getDescription(), product.getId()};
        return customJdbcTemplate.update(connection, SQL_UPDATE_PRODUCT, args, product);
    }

    @Override
    public boolean delete(Product product) throws DaoException {
        return delete(product.getId());
    }

    @Override
    public boolean delete(long id) throws DaoException {
        Object[] args = {id};
        try {
            return customJdbcTemplate.update(connection, SQL_DELETE_PRODUCT_BY_ID, args) >= 0;
        } catch (DaoException e) {
            logger.error("failed to delete product with id {}", id, e);
            throw new DaoException("failed to product item", e);
        }
    }

    @Override
    public int countAllProducts() throws DaoException {
        return customJdbcTemplate.query(connection, SQL_COUNT_PRODUCTS);
    }
}


