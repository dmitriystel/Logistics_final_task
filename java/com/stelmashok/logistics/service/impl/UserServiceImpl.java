package com.stelmashok.logistics.service.impl;

import com.stelmashok.logistics.model.dao.AbstractDao;
import com.stelmashok.logistics.model.dao.TransactionManager;
import com.stelmashok.logistics.model.dao.UserDao;
import com.stelmashok.logistics.model.dao.impl.CarrierDaoImpl;
import com.stelmashok.logistics.model.dao.impl.UserDaoImpl;
import com.stelmashok.logistics.model.entity.Product;
import com.stelmashok.logistics.model.entity.User;
import com.stelmashok.logistics.exception.DaoException;
import com.stelmashok.logistics.exception.ServiceException;
import com.stelmashok.logistics.exception.TransactionException;
import com.stelmashok.logistics.service.UserService;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
// откуда передаются данные в методы?
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static UserService instance;

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }
    //  ok service - dao
    @Override
    public boolean createUser(String customerName, String login, String email, String password, String name,
                              String surname, String phone) throws ServiceException {
        AbstractDao userDao = new UserDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(userDao);
            try {
                User user = createUserObject(customerName, login, email, password, name, surname, phone
                );
                boolean result = userDao.create(user);
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

    private User createUserObject(String customerName, String login, String email, String password, String name,
                                  String surname, String phone) {
        User user = new User();
        user.setCustomerName(customerName);
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setPhone(phone);
        return user;

    }
    //  ok service - dao
    @Override
    public boolean isEmailExist(String email) throws ServiceException {
        AbstractDao userDao = new UserDaoImpl();
        boolean result = false;
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(userDao);
            try {
                result = ((UserDaoImpl) userDao).isEmailExist(email);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed to check if user with {} exists", email, e);
        }
        return result;
    }
    //  ok service - dao
    @Override
    public List<User> findAllUsers() throws ServiceException {
        AbstractDao userDao = new UserDaoImpl();
        List<User> users;
        try (TransactionManager transactionManager  = new TransactionManager()){
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(userDao);
            try {
                users = ((UserDaoImpl) userDao).findAllUsers();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw  new ServiceException(e);
        }
        return users;
    }
    @Override
    public Optional<User> findUserByEmail(String email) throws ServiceException {
        AbstractDao userDao = new UserDaoImpl();
        Optional<User> user = Optional.empty();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(userDao);
            try {
                user = ((UserDao) userDao).findByEmail(email);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed to find user by email", e);
            throw new ServiceException(e);
        }
        return user;
    }
    //  удалить или сделать?
    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public List<User> findAllPaginatedUsers(int currentPage, int usersPerPage) throws ServiceException {
        AbstractDao userDao = new UserDaoImpl();
        List<User> users;
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(userDao);
            try {
                users = ((UserDaoImpl) userDao).findAllPaginatedUsers(currentPage, usersPerPage);
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
                throw new ServiceException(e);
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction", e);
            throw new ServiceException(e);
        }

        return users;
    }

    @Override
    public int countAllUsers() throws ServiceException {
        AbstractDao userDao = new UserDaoImpl();
        int numberOfUsers = 0;
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(userDao);
            try {
                numberOfUsers = ((CarrierDaoImpl) userDao).countAllCarriers();
                transactionManager.commit();
            } catch (DaoException e) {
                transactionManager.rollback();
            }
        } catch (TransactionException e) {
            logger.error("failed perform a transaction",e);
        }
        return numberOfUsers;
    }

    @Override   //  как сделать?
    public boolean changePassword(User user, String newPassword) throws ServiceException {
        AbstractDao userDao = new UserDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(userDao);
            try {
                Optional<User> resul = ((UserDaoImpl) userDao).changePassword(user, passwordHashGenerator.generatePasswordHash(newPassword).get());
                if (resul.isPresent()) {
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


    /*
    String customerName, String login, String email, String password, String name,
                                  String surname, String phone
     */
    @Override
    public boolean updateUser(User user, long userId, String customerName, String userEmail, List<Part> parts) throws ServiceException {
        AbstractDao userDao = new UserDaoImpl();
        try (TransactionManager transactionManager = new TransactionManager()) {
    // create connection or take from pool then put in usedConnections, get an array dao and everyone gets a connection
            transactionManager.beginTransaction(userDao);
            try {
                Optional<User> userForUpdate = userDao.findById(userId);
                User updateUser = updateUserObject(user, customerName, userEmail);
                Optional<Product> result = userDao.update(updateUser);
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

    private User updateUserObject(User user, String customerName, String userEmail) {
        user.setCustomerName(customerName);
        user.setEmail(userEmail);
        return user;
    }



}
