package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.model.entity.Carrier;
import com.stelmashok.logistics.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface CarrierDao {

    boolean isExistCarrier(String carrierName) throws DaoException;

    List<Carrier> findAllCarriers() throws DaoException;

    List<Carrier> findAllPaginatedCarriers(int currentPage, int carriersPerPage) throws DaoException;

    Optional<Carrier> findByCarrierName(String carrierName) throws DaoException;

    int countAllCarriers() throws DaoException;
//  Почему горит серым? Метод реализован
    boolean isExistTruck(String truckNumber) throws DaoException;








}
