package com.stelmashok.logistics.service.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.CarrierDao;
import com.stelmashok.logistics.model.dao.TransactionManager;
import com.stelmashok.logistics.model.dao.impl.CarrierDaoImpl;
import com.stelmashok.logistics.model.entity.Carrier;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.exception.ServiceException;
import com.stelmashok.logistics.exception.TransactionException;
import com.stelmashok.logistics.service.CarrierService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
// откуда передаются данные в методы?
// findById есть в дао, нету тут. Подумать, возможно не нужен поиска перевозчика по id
public class CarrierServiceImpl implements CarrierService {

    private static final Logger logger = LogManager.getLogger();
    private static CarrierService instance;

    private CarrierServiceImpl() {
    }

    public static CarrierService getInstance() {
        if (instance == null) {
            instance = new CarrierServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean createCarrier(String carrierName, String truckNumber) throws ServiceException {
        AbstractDao carrierDao = new CarrierDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao);
            try {
                // object is created and values are set by set and get methods
                Carrier carrier = createCarrierObject(carrierName, truckNumber);
                boolean result = carrierDao.create(carrier);
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

    private Carrier createCarrierObject(String carrierName, String truckNumber) {
        Carrier carrier = new Carrier();
        carrier.setCarrierName(carrierName);
        carrier.setTruckNumber(truckNumber);
        return carrier;
    }

    @Override
    public boolean isExistCarrier(String carrierName) throws ServiceException {
        AbstractDao carrierDao = new CarrierDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao); // create connection, disable autocommit
            try {
                boolean result = ((CarrierDaoImpl) carrierDao).isExistCarrier(carrierName);
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
    public boolean isExistTruck(String truckNumber) throws ServiceException {
        AbstractDao carrierDao = new CarrierDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
     // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao);
            try {
                boolean result = ((CarrierDaoImpl) carrierDao).isExistTruck(truckNumber);
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
    public List<Carrier> findAllCarriers() throws ServiceException {
        AbstractDao carrierDao = new CarrierDaoImpl();
        List<Carrier> carriers;
        try (TransactionManager transactionManager  = new TransactionManager()){
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao);
            try {
                    carriers = ((CarrierDaoImpl) carrierDao).findAllCarriers();
                    transactionManager.commit();
                } catch (DaoException e) {
                    transactionManager.rollback();
                    throw new ServiceException(e);
                }
            } catch (TransactionException e) {
                logger.error("failed perform a transaction", e);
                throw  new ServiceException(e);
        }
        return carriers;
    }
    @Override
    public List<Carrier> findAllPaginatedCarriers(int currentPage, int carriersPerPage) throws ServiceException {
        AbstractDao carrierDao = new CarrierDaoImpl();
        List<Carrier> carriers;
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao);
            try {
                carriers = ((CarrierDaoImpl) carrierDao).findAllPaginatedCarriers(currentPage, carriersPerPage);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw new ServiceException(e);
        }
            return carriers;
        }

    @Override
    public Optional<Carrier> findByCarrierName(String carrierName) throws ServiceException {
        AbstractDao carrierDao = new CarrierDaoImpl(); // obj для вызова методов класса CarrierDaoImpl
        Optional<Carrier> carrier = Optional.empty();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao);
            try {
                carrier = ((CarrierDao) carrierDao).findByCarrierName(carrierName);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw  new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed to find carrier by carrier name", e);
            throw new ServiceException(e);
        }
        return carrier;
    }

    @Override
    public int countAllCarriers() throws ServiceException {
        AbstractDao carrierDao = new CarrierDaoImpl();
        int numberOfCarriers = 0;
        try (TransactionManager transactionManager = new TransactionManager()) {
// create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao);
            try {
                numberOfCarriers = ((CarrierDaoImpl) carrierDao).countAllCarriers();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction",e);
        }
        return numberOfCarriers;
    }

    @Override
    public boolean updateCarrier(long carrierId, String carrierName, String truckNumber) {
        AbstractDao carrierDao = new CarrierDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao);
            try {
                Optional<Carrier> carrierForUpdate = carrierDao.findById(carrierId);
                Carrier updateCarrier = updateCarrierObject(carrierForUpdate.get(), carrierName, truckNumber);
                Optional<Carrier> result = carrierDao.update(updateCarrier);
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

    private Carrier updateCarrierObject(Carrier carrierForUpdate, String carrierName, String truckNumber) {
        carrierForUpdate.setCarrierName(carrierName);
        carrierForUpdate.setTruckNumber(truckNumber);
        return carrierForUpdate;
    }

    @Override
    public boolean deleteCarrier(long carrierId) throws ServiceException {
        AbstractDao carrierDao = new CarrierDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(carrierDao);
            try {
                boolean result = carrierDao.delete(carrierId);
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
