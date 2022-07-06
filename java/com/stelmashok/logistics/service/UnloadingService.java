package com.stelmashok.logistics.service;


import com.stelmashok.logistics.model.entity.Unloading;
import com.stelmashok.logistics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UnloadingService {
    boolean isExistUnloading(String city) throws ServiceException;

    List<Unloading> findAllUnloadings() throws ServiceException;

    Optional<Unloading> findByCountry(String country) throws ServiceException;

    Optional<Unloading> findByCity(String city) throws ServiceException;

    List<Unloading> findAllPaginatedUnloadings(int currentPage, int carriersPerPage) throws ServiceException;

    int countAllUnloadings() throws ServiceException;

    boolean createUnloading(String country, String city) throws ServiceException;

    boolean update(long unloadingId, String country, String city) throws ServiceException;

    boolean delete(long unloadingId) throws ServiceException;

}

/*
    //  не испльзуется из Dao
    public abstract Optional<T> findById(long id) throws DaoException; // Optional<T> что это?
    public abstract boolean delete(T t) throws DaoException;
 */