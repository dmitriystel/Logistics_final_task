package com.stelmashok.logistics.service.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.TransactionManager;
import com.stelmashok.logistics.model.dao.UnloadingDao;
import com.stelmashok.logistics.model.dao.impl.ProductDaoImpl;
import com.stelmashok.logistics.model.dao.impl.UnloadingDaoImpl;
import com.stelmashok.logistics.model.entity.Unloading;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.exception.ServiceException;
import com.stelmashok.logistics.exception.TransactionException;
import com.stelmashok.logistics.service.UnloadingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
public class UnloadingServiceImpl implements UnloadingService {

        private static final Logger logger = LogManager.getLogger();
        private static UnloadingService instance;

        private UnloadingServiceImpl() {
        }

        public static UnloadingService getInstance() {
            if (instance == null) {
                instance = new UnloadingServiceImpl();
            }
            return instance;
        }

    @Override
    public boolean isExistUnloading(String city) throws ServiceException {
        AbstractDao unloadingDao = new UnloadingDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(unloadingDao);
            try {
                boolean result = ((UnloadingDaoImpl) unloadingDao).isExistUnloading(city);
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
    public List<Unloading> findAllUnloadings() throws ServiceException {
        AbstractDao unloadingDao = new UnloadingDaoImpl();
        List<Unloading> unloading;
        try (TransactionManager transactionManager  = new TransactionManager()){
            transactionManager.beginTransaction(unloadingDao);
            try {
                unloading = ((UnloadingDaoImpl) unloadingDao).findAllUnloadings();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw  new ServiceException(e);
        }
        return unloading;
    }
    @Override
    public Optional<Unloading> findByCountry(String country) throws ServiceException {
        AbstractDao unloadingDao = new UnloadingDaoImpl();
        Optional<Unloading> unloading = Optional.empty();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(unloadingDao);
            try {
                unloading = ((UnloadingDao) unloadingDao).findByCountry(country);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw  new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed to find carrier by carrier name", e);
            throw new ServiceException(e);
        }
        return unloading;
    }

    @Override
    public Optional<Unloading> findByCity(String city) throws ServiceException {
        AbstractDao unloadingDao = new UnloadingDaoImpl();
        Optional<Unloading> unloading = Optional.empty();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(unloadingDao);
            try {
                unloading = ((UnloadingDao) unloadingDao).findByCity(city);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed to find unloading by city", e);
            throw new ServiceException(e);
        }
        return unloading;
    }

    @Override
    public List<Unloading> findAllPaginatedUnloadings(int currentPage, int unloadingsPerPage) throws ServiceException {
            AbstractDao unloadingDao = new UnloadingDaoImpl();
        List<Unloading> unloadings;
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(unloadingDao);
            try {
                unloadings = ((UnloadingDaoImpl) unloadingDao).findAllPaginatedUnloadings(currentPage, unloadingsPerPage);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw new ServiceException(e);
        }

        return unloadings;
    }

    @Override
    public int countAllUnloadings() throws ServiceException {
        AbstractDao unloadingDao = new UnloadingDaoImpl();
        int numberOfUnloadings = 0;
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(unloadingDao);
            try {
                numberOfUnloadings = ((ProductDaoImpl) unloadingDao).countAllProducts();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction",e);
        }
        return numberOfUnloadings;
    }

    @Override
    public boolean createUnloading(String country, String city) throws ServiceException {
        AbstractDao unloadingDao = new UnloadingDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
            transactionManager.beginTransaction(unloadingDao);
            try {
                Unloading unloading = createUnloadingObject(country, city);
                boolean result = unloadingDao.create(unloading);
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

        private Unloading createUnloadingObject(String country, String city) {
            Unloading unloading = new Unloading();
            unloading.setCountry(country);
            unloading.setCity(city);
            return unloading;
        }

        @Override
    public boolean update(long unloadingId, String country, String city) throws ServiceException {
            AbstractDao unloadingDao = new UnloadingDaoImpl();
            try (TransactionManager transactionManager = new TransactionManager()) {
                transactionManager.beginTransaction(unloadingDao);
                try {
                    Optional<Unloading> unloadingForUpdate = unloadingDao.findById(unloadingId);
                    Unloading updateUnloading = updateUnloadingObject(unloadingForUpdate.get(), country, city);
                    Optional<Unloading> result = unloadingDao.update(updateUnloading);
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

        private Unloading updateUnloadingObject(Unloading categoryForUpdate, String country, String city) {
            categoryForUpdate.setCountry(country);
            categoryForUpdate.setCity(city);
            return categoryForUpdate;
        }

        @Override
    public boolean delete(long unloadingId) throws ServiceException {
            AbstractDao unloadingDao = new UnloadingDaoImpl();
            try (TransactionManager transactionManager = new TransactionManager()) {
                transactionManager.beginTransaction(unloadingDao);
                try {
                    boolean result = unloadingDao.delete(unloadingId);
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
