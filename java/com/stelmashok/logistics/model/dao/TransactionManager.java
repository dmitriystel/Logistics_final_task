package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.exception.TransactionException;
import com.stelmashok.logistics.model.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class TransactionManager implements AutoCloseable{
    private static final Logger logger = LogManager.getLogger();
    private Connection connection;

    public void beginTransaction(AbstractDao... dao) throws TransactionException {
        if (connection == null) {
            connection = ConnectionPool.getInstance().acquireConnection(); // create connection or take from pool
        }                                                                  // put connection in usedConnections
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("failed to begin a transaction", e);
            throw new TransactionException("failed begin a transaction", e);
        }
        Arrays.stream(dao).forEach(d -> d.setConnection(connection)); // get an array dao and everyone gets a connection
    }

    @Override
    public void close() throws TransactionException {
        if (connection == null) {
            logger.error("failed to end the transaction because the connection is null");
            throw new TransactionException("failed to end the transaction because the connection is null");
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("failed to end a transaction", e);
            throw new TransactionException("failed to end a transaction", e);
        } // the connection is removed from usedConnections and put in availableConnections
        boolean result = ConnectionPool.getInstance().releaseConnection(connection);
        if (!result) {
            logger.error("failed to release a connection");
            throw new TransactionException("failed to release a connection");
        }
        connection = null;
    }

    public void commit() throws TransactionException {
        if (connection == null) {
            logger.error("failed to commit a transaction because the connection is null");
            throw new TransactionException("failed to commit a transaction because the connection is null");
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error("failed to commit a transaction", e);
            throw new TransactionException("failed to commit a transaction", e);
        }
    }

    public void rollback() throws TransactionException {
        if (connection == null) {
            logger.error("failed to rollback a transaction because the connection is null");
            throw new TransactionException("failed to rollback a transaction because the connection is null");
        }
        try {
            connection.rollback(); // transaction failed on error
        } catch (SQLException e) {
            logger.error("failed to rollback a transaction", e);
            throw new TransactionException("failed to rollback a transaction", e);
        }
    }
}
