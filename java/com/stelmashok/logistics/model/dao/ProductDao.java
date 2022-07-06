package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.model.entity.Product;
import com.stelmashok.logistics.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    boolean isExistProduct(String title) throws DaoException;

    List<Product> findAllProducts() throws DaoException;

    List<Product> findAllPaginatedProducts(int currentPage, int productsPerPage) throws DaoException;

    Optional<Product> findByTitle(String title) throws DaoException;

    int countAllProducts() throws DaoException;



}
