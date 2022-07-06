package com.stelmashok.logistics.service;

import com.stelmashok.logistics.model.entity.*;
import com.stelmashok.logistics.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
   boolean isExistUnloading(String city) throws ServiceException;

    List<Unloading> findAllUnloadings() throws ServiceException;

    Optional<Unloading> findByCountry(String country) throws ServiceException;

    Optional<Unloading> findByCity(String city) throws ServiceException;

    List<Unloading> findAllPaginatedUnloadings(int currentPage, int carriersPerPage) throws ServiceException;

    int countAllUnloadings() throws ServiceException;

    boolean createUnloading(String country, String city) throws ServiceException;

    boolean update(long unloadingId, String country, String city) throws ServiceException;

    boolean delete(long unloadingId) throws ServiceException;

 */
public interface TransportOrderService {

    boolean isExistTransportOrder(LocalDateTime transportOrderId) throws ServiceException;

    List<TransportOrder> findAllTransportOrders() throws ServiceException;

    Optional<TransportOrder> findByCustomerName(String customerName) throws ServiceException;

    List<TransportOrder> findAllPaginatedTransportOrders(int currentPage, int transportOrdersPerPage) throws ServiceException;

    int countAllTransportOrders() throws ServiceException;
    //  Верно? какие данные должны быть? как взять данные из других таблиц?
    boolean createTransportOrders(User user, LocalDateTime orderDate, LocalDateTime deliveryDate, Product product, Unloading unloading, Carrier carrier  ) throws ServiceException;

    boolean update(long transportOrderId, User user, LocalDateTime orderDate, LocalDateTime deliveryDate, Product product, Unloading unloading, Carrier carrier) throws ServiceException;

    public abstract boolean delete(long transportOrderId) throws ServiceException;

}
/*
    //  не испльзуется из Dao
    public abstract Optional<T> findById(long id) throws DaoException; // Optional<T> что это?
    public abstract boolean delete(T t) throws DaoException;

 */