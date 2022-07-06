package com.stelmashok.logistics.service;

import com.stelmashok.logistics.model.entity.Product;
import com.stelmashok.logistics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    boolean isExistProduct(String title) throws ServiceException;

    List<Product> findAllProducts() throws ServiceException;

    List<Product> findAllPaginatedProducts(int currentPage, int productsPerPage) throws ServiceException;

    Optional<Product> findByTitle(String title) throws ServiceException;

    int countAllProducts() throws ServiceException;

    boolean createProduct(String title, int weight, String description) throws ServiceException;

    boolean updateProduct(long productId, String title, int weight, String description) throws ServiceException;

    boolean deleteProduct(long productId) throws ServiceException;
}
/*
    //  не используется из Dao
    public abstract Optional<T> findById(long id) throws DaoException; // Optional<T> что это?
    public abstract boolean delete(T t) throws DaoException;
 */