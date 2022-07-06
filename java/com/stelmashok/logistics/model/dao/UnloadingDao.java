package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.model.entity.Unloading;
import com.stelmashok.logistics.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface UnloadingDao {
    boolean isExistUnloading(String city) throws DaoException;

    List<Unloading> findAllUnloadings() throws DaoException;

    Optional<Unloading> findByCountry(String country) throws DaoException;

    Optional<Unloading> findByCity(String city) throws DaoException;

    List<Unloading> findAllPaginatedUnloadings(int currentPage, int carriersPerPage) throws DaoException;
    
    int countAllUnloadings() throws DaoException;
}
