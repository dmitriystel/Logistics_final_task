package com.stelmashok.logistics.service.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.ProductDao;
import com.stelmashok.logistics.model.dao.TransactionManager;
import com.stelmashok.logistics.model.dao.impl.ProductDaoImpl;
import com.stelmashok.logistics.model.entity.Product;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.exception.ServiceException;
import com.stelmashok.logistics.exception.TransactionException;
import com.stelmashok.logistics.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger();
    private static ProductService instance;

    private ProductServiceImpl() {
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean isExistProduct(String title) throws ServiceException {
        AbstractDao productDao = new ProductDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(productDao);
            try {
                boolean result = ((ProductDaoImpl) productDao).isExistProduct(title);
                if (result) {
                    transactionManager.commit();
                    return true;
                }
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed to perform a transaction", e);
        }
        return false;
    }

    @Override
    public List<Product> findAllProducts() throws ServiceException {
        AbstractDao productDao = new ProductDaoImpl();
        List<Product> products;
        try (TransactionManager transactionManager  = new TransactionManager()){
            transactionManager.beginTransaction(productDao);
            try {
                products = ((ProductDaoImpl) productDao).findAllProducts();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw  new ServiceException(e);
        }
        return products;
    }

    @Override
    public List<Product> findAllPaginatedProducts(int currentPage, int productsPerPage) throws ServiceException {
        AbstractDao productDao = new ProductDaoImpl();
        List<Product> products;
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(productDao);
            try {
                products = ((ProductDaoImpl) productDao).findAllPaginatedProducts(currentPage, productsPerPage);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw new ServiceException(e);
        }

            return products;
}

    @Override
    public Optional<Product> findByTitle(String title) throws ServiceException {
        AbstractDao productDao = new ProductDaoImpl();
        Optional<Product> product = Optional.empty();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(productDao);
            try {
                product = ((ProductDao) productDao).findByTitle(title);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw  new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed to find carrier by carrier name", e);
            throw new ServiceException(e);
        }
        return product;
    }

    @Override
    public int countAllProducts() throws ServiceException {
        AbstractDao productDao = new ProductDaoImpl();
        int numberOfProducts = 0;
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(productDao);
            try {
                numberOfProducts = ((ProductDaoImpl) productDao).countAllProducts();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction",e);
        }
        return numberOfProducts;
    }

    @Override
    public boolean createProduct(String title, int weight, String description) throws ServiceException {
        AbstractDao productDao = new ProductDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(productDao);
            try {
                Product product = createProductObject(title, weight, description);
                boolean result = productDao.create(product);
                if (result) {
                    transactionManager.commit();
                    return true;
                }
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
        }
        return false;
    }

    private Product createProductObject(String title, int weight, String description) {
        Product product = new Product();
        product.setTitle(title);
        product.setWeight(weight);
        product.setDescription(description);
        return product;
    }

    @Override
    public boolean updateProduct(long productId, String title, int weight, String description) throws ServiceException {
        AbstractDao productDao = new ProductDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(productDao);
            try {
                Optional<Product> productForUpdate = productDao.findById(productId);
                Product updateProduct = updateProductObject(productForUpdate.get(), title, weight, description);
                Optional<Product> result = productDao.update(updateProduct);
                if (result.isPresent()) {
                    transactionManager.commit();
                    return true;
                }
            } catch (DaoException t) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
        }
        return false;
    }

    private Product updateProductObject(Product categoryForUpdate, String title, int weight, String description) {
        categoryForUpdate.setTitle(title);
        categoryForUpdate.setWeight(weight);
        categoryForUpdate.setDescription(description);
        return categoryForUpdate;
    }

    @Override
    public boolean deleteProduct(long productId) throws ServiceException {
        AbstractDao productDao = new ProductDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(productDao);
            try {
                boolean result = productDao.delete(productId);
                if (result) {
                    transactionManager.commit();
                    return true;
                }
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
        }
        return false;
    }
    }

