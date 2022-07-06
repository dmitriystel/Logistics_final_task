package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.model.entity.*;
import com.stelmashok.logistics.exception.DaoException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransportOrderDao {
    boolean isExistTransportOrder(LocalDateTime orderDate) throws DaoException;

    List<TransportOrder> findAllTransportOrders() throws DaoException;

    Optional<TransportOrder> findByCustomerName(String customerName) throws DaoException;

    List<TransportOrder> findAllPaginatedTransportOrders(int currentPage, int transportOrdersPerPage) throws DaoException;

    int countAllTransportOrders() throws DaoException;



}
