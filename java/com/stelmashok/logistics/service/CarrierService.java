package com.stelmashok.logistics.service;

import com.stelmashok.logistics.model.entity.Carrier;
import com.stelmashok.logistics.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface CarrierService {

    boolean isExistCarrier(String carrierName) throws ServiceException;

    List<Carrier> findAllCarriers() throws ServiceException;

    List<Carrier> findAllPaginatedCarriers(int currentPage, int carriersPerPage) throws ServiceException;

    Optional<Carrier> findByCarrierName(String carrierName) throws ServiceException;

    int countAllCarriers() throws ServiceException;

    boolean isExistTruck(String truckNumber) throws ServiceException;

    boolean createCarrier(String carrierName, String carrierNumber) throws ServiceException;

    boolean updateCarrier(long carrierId, String carrierName, String truckNumber);

    boolean deleteCarrier(long carrierId) throws ServiceException;

/*
    // не используется из Dao
    public abstract Optional<T> findById(long id) throws DaoException; // Optional<T> что это?
    public abstract boolean delete(T t) throws DaoException;
 */

}


