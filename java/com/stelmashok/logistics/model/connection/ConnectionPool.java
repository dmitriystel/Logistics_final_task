package com.stelmashok.logistics.model.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    // One atomic action passes, the flag changes and all threads see the last change.
    // Non-blocking synchronization - data which we work with are not completely blocked, but change quickly enough
   // to be visible to all threads.
    private static final AtomicBoolean isPoolCreated = new AtomicBoolean(false);
    // ReentrantLock represents a replacement of the Synchronized mechanism
    // it is blocked/unlocked by lock/unlock methods by itself
    // lock - blocks passage through a section of code
    // after lock, only one thread works, the rest of the threads will stand and wait until the thread calls unlock
    // threads will go through the lock in the order in which they stopped before it
    // All waiting threads will access in approach order, in fair order (with the parameter true)
    private static final Lock locker = new ReentrantLock(true); // for independent thread execution
    private static final int POOL_SIZE = 5;
    private static ConnectionPool instance;
    // BlockingDeque stops a thread requesting an element from a queue until it finds an element available for retrieval.
    // when a connection is closed, the connection is put in availableConnections
    private final BlockingDeque<ProxyConnection> availableConnections;
    private final BlockingDeque<ProxyConnection> usedConnections; // busy connections
    // constructor creates and put 5 connections in the availableConnections variable - creates a connection pool
    private ConnectionPool() {
        availableConnections = new LinkedBlockingDeque<>(POOL_SIZE);
        usedConnections = new LinkedBlockingDeque<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                ProxyConnection connection = ConnectionFactory.createConnection();
                availableConnections.put(connection); // If the queue is full, waits for space to become available.
            } catch (SQLException | InterruptedException e) {
                logger.error("failed to create a connection pool");
            }
        }
        if (availableConnections.isEmpty()) {
            logger.fatal("the connection pool is empty, no connections created");
            throw new RuntimeException("the connection pool is empty, no connections were created");
        }
        logger.info("connection pool has been successfully created");
    }
// if the ConnectionPool is not created, the thread is blocked, the ConnectionPool is created and the thread is unblocked
    public static ConnectionPool getInstance() {
        if (!isPoolCreated.get()) {
            try {
                locker.lock(); // locking
                if (instance == null) {
                    instance = new ConnectionPool();
                    isPoolCreated.set(true);
                }
            } finally {
                locker.unlock();

            }
        }
        return instance;
    }
    // take the connection to use from the available connections and put it in the used connections
    public Connection acquireConnection() {
        ProxyConnection connection = null;
        try {
        // Take and removes an element from the queue. If the queue is empty, waits for an element to appear there.
            connection = availableConnections.take();
        // Adds an element to the queue. If the queue is full, it waits for space to become available.
            usedConnections.put(connection);
        } catch (InterruptedException e) {
            logger.error("failed to acquire available connection", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }
// if connection is a Proxy Connection, the connection is removed from usedConnections and put in availableConnections
    public boolean releaseConnection(Connection connection) {
        if (!(connection instanceof ProxyConnection)) {
            logger.error("wrong instance");
            return false;
        }
        try { // if connection instance of ProxyConnection
            usedConnections.remove(connection);
            availableConnections.put((ProxyConnection) connection);
            return true;
        } catch (InterruptedException e) {
            logger.error("failed to release a connection", e);
            Thread.currentThread().interrupt();
        }
        return false;
    }
    // extract all connections from availableConnections and close
    public void destroyPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                availableConnections.take().reallyClose();
            } catch (SQLException e) {
                logger.error("failed to destroy pool", e);
            } catch (InterruptedException e) {
                logger.error("failed to destroy pool", e);
                Thread.currentThread().interrupt();
            }
        }
        deregisterDrivers();
    }
    // When the application terminates, unload or deregister the driver.
    private void deregisterDrivers() {
        // get all open drivers and close them
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("failed to deregister driver", e);
            }
        });
    }
}