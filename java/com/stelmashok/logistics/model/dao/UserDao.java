package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.model.entity.User;
import com.stelmashok.logistics.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    // boolean createUser(User user) throws DaoException; - удалить

    boolean isEmailExist(String email) throws DaoException;

    Optional<User> findByEmail(String email) throws DaoException;

    Optional<User> findUserByEmailAndPassword(String email, String password) throws DaoException;

    List<User> findAllUsers() throws DaoException;

    List<User> findAllPaginatedUsers(int currentPage, int usersPerPage) throws DaoException;

    int countAllUsers() throws DaoException;

    Optional<User> changePassword(User user, String newPassword) throws DaoException;
}
