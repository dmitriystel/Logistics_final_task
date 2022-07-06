package com.stelmashok.logistics.service.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.TransactionManager;
import com.stelmashok.logistics.model.dao.TransportOrderDao;
import com.stelmashok.logistics.model.dao.impl.TransportOrderDaoImpl;
import com.stelmashok.logistics.model.entity.*;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.exception.ServiceException;
import com.stelmashok.logistics.exception.TransactionException;
import com.stelmashok.logistics.service.TransportOrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TransportOrderServiceImpl implements TransportOrderService {
    private static final Logger logger = LogManager.getLogger();
    private static TransportOrderService instance;

    private TransportOrderServiceImpl() {
    }

    public static TransportOrderService getInstance() {
        if (instance == null) {
            instance = new TransportOrderServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean isExistTransportOrder(LocalDateTime transportOrderId) throws ServiceException {
        AbstractDao transportOrderDao = new TransportOrderDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(transportOrderDao);
            try {
                boolean result = ((TransportOrderDaoImpl) transportOrderDao).isExistTransportOrder(transportOrderId);
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
    public List<TransportOrder> findAllTransportOrders() throws ServiceException {
        AbstractDao transportOrderDao = new TransportOrderDaoImpl();
        List<TransportOrder> transportOrders;
        try (TransactionManager transactionManager  = new TransactionManager()){
            transactionManager.beginTransaction(transportOrderDao);
            try {
                transportOrders = ((TransportOrderDaoImpl) transportOrderDao).findAllTransportOrders();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw  new ServiceException(e);
        }
        return transportOrders;
    }

    @Override
    public Optional<TransportOrder> findByCustomerName(String customerName) throws ServiceException {
        AbstractDao trasportOrderDao = new TransportOrderDaoImpl();
        Optional<TransportOrder> transportOrder = Optional.empty();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(trasportOrderDao);
            try {
                transportOrder = ((TransportOrderDao) trasportOrderDao).findByCustomerName(customerName);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw  new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed to find carrier by carrier name", e);
            throw new ServiceException(e);
        }
        return transportOrder;
    }

    @Override
    public List<TransportOrder> findAllPaginatedTransportOrders(int currentPage, int transportOrdersPerPage) throws ServiceException {
        AbstractDao transportOrderDao = new TransportOrderDaoImpl();
        List<TransportOrder> transportOrders;
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(transportOrderDao);
            try {
                transportOrders = ((TransportOrderDaoImpl) transportOrderDao).findAllPaginatedTransportOrders(currentPage, transportOrdersPerPage);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw new ServiceException(e);
        }

        return transportOrders;
    }

    @Override
    public int countAllTransportOrders() throws ServiceException {
        AbstractDao transportOrderDao = new TransportOrderDaoImpl();
        int numberOfTransportOrders = 0;
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(transportOrderDao);
            try {
                numberOfTransportOrders = ((TransportOrderDaoImpl) transportOrderDao).countAllTransportOrders();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction",e);
        }
        return numberOfTransportOrders;
    }

    @Override
    public boolean createTransportOrders(User user, LocalDateTime orderDate, LocalDateTime deliveryDate, Product product, Unloading unloading, Carrier carrier) throws ServiceException {
        AbstractDao transportOrderDao = new TransportOrderDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(transportOrderDao);
            try {
                TransportOrder transportOrder = createTransportOrderObject(user, orderDate, deliveryDate, product, unloading, carrier);
                boolean result = transportOrderDao.create(product);
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

    private TransportOrder createTransportOrderObject(User user, LocalDateTime orderDate, LocalDateTime deliveryDate, Product product, Unloading unloading, Carrier carrier) {
        TransportOrder transportOrder = new TransportOrder();
        transportOrder.setUser(user);
        transportOrder.setOrderDate(orderDate);
        transportOrder.setDeliveryDate(deliveryDate);
        transportOrder.setProduct(product);
        transportOrder.setUnloading(unloading);
        transportOrder.setCarrier(carrier);
        return transportOrder;

    }

    @Override
    public boolean update(long transportOrderId, User user, LocalDateTime orderDate, LocalDateTime deliveryDate, Product product, Unloading unloading, Carrier carrier) throws ServiceException {
        AbstractDao transportOrderDao = new TransportOrderDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(transportOrderDao);
            try {
                Optional<TransportOrder> productForUpdate = transportOrderDao.findById(transportOrderId);
                TransportOrder updateTransportOrder = updateTransportOrderObject(productForUpdate.get(), user, orderDate, deliveryDate, product, unloading, carrier);
                Optional<Product> result = transportOrderDao.update(updateTransportOrder);
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

    private TransportOrder updateTransportOrderObject(TransportOrder categoryForUpdate, User user, LocalDateTime orderDate,
                                                      LocalDateTime deliveryDate, Product product, Unloading unloading, Carrier carrier) {
        categoryForUpdate.setUser(user);
        categoryForUpdate.setOrderDate(orderDate);
        categoryForUpdate.setDeliveryDate(deliveryDate);
        categoryForUpdate.setProduct(product);
        categoryForUpdate.setUnloading(unloading);
        categoryForUpdate.setCarrier(carrier);
        return categoryForUpdate;


    }

    @Override
    public boolean delete(long transportOrderId) throws ServiceException {
        AbstractDao transportOrderDao = new TransportOrderDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(transportOrderDao);
            try {
                boolean result = transportOrderDao.delete(transportOrderId);
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
}
