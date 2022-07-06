package com.stelmashok.logistics.service;

import com.stelmashok.logistics.model.entity.User;
import com.stelmashok.logistics.exception.ServiceException;

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
public interface UserService {

    boolean createUser(String customerName, String login, String email, String password, String name, String surname, String phone) throws ServiceException;

    boolean isEmailExist(String email) throws ServiceException;

    Optional<User> findUserByEmail(String email) throws ServiceException;

    Optional<User> findUserByEmailAndPassword(String email, String password) throws ServiceException;

    List<User> findAllUsers() throws ServiceException;

    List<User> findAllPaginatedUsers(int currentPage, int usersPerPage) throws ServiceException;

    int countAllUsers() throws ServiceException;

    boolean changePassword(User user, String newPassword) throws ServiceException;
    //  верно ли?
    boolean updateUser(User user, String customerName, String login, String email, String password, String phone) throws ServiceException;
}
/*
    //  не испльзуется из Dao
    public abstract Optional<T> findById(long id) throws DaoException; // Optional<T> что это?

    //  не используется
    public abstract boolean delete(T t) throws DaoException;
    public abstract boolean delete(long id) throws DaoException;
 */