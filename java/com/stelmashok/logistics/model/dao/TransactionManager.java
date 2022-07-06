package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.exception.TransactionException;
import com.stelmashok.logistics.model.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
/*
Интерфейс AutoCloseable представляет объект-хранилище некоего ресурса, пока тот не закрыт.
В единственном его методе close() объявляется логика закрытия этого ресурса.
 */
public class TransactionManager implements AutoCloseable{
    private static final Logger logger = LogManager.getLogger();
    private Connection connection;  //  объявляется для доступа к методам класса через объект connection
/*
Для фиксации результатов работы SQL-операторов, логически выполняемых в рамках некоторой транзакции, используется
SQL-оператор COMMIT. В API JDBC эта операция выполняется по умолчанию после каждого вызова методов executeQuery() и
executeUpdate(). Если же необходимо сгруппировать запросы и только после этого выполнить операцию COMMIT, сначала
вызывается метод setAutoCommit(boolean param) интерфейса Connection с параметром false, в результате выполнения
которого текущее соединение с БД переходит в режим неавтоматического подтверждения операций. После этого выполнение
любого запроса на изменение информации в таблицах базы данных не приведет к необратимым последствиям, пока операция
COMMIT не будет выполнена непосредственно. Подтверждает выполнение SQL-запросов метод commit() интерфейса Connection,
в результате действия которого все изменения таблицы производятся как одно логическое действие. Если же транзакция
не выполнена, то методом rollback() отменяются действия всех запросов SQL, начиная от последнего вызова commit().
В следующем примере информация добавляется в таблицу в режиме действия транзакции, подтвердить или отменить действия
которой можно, снимая или добавляя комментарий в строках вызова методов commit() и rollback().
 */

    public void beginTransaction(AbstractDao... dao) throws TransactionException {
        if (connection == null) {
            connection = ConnectionPool.getInstance().acquireConnection(); // create connection or take from pool
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("failed to begin a transaction", e);
            throw new TransactionException("failed begin a transaction", e);
        }
        Arrays.stream(dao).forEach(d -> d.setConnection(connection)); // ?
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
        }
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

// if the transaction isn't completed, rollback() method cancels the actions of all SQL queries after the last commit().
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
