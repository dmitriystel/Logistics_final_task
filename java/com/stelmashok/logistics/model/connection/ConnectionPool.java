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
/*
Что такое ConnectionPool?
Это контейнер с соединениями где они создаются по размеру пула, т.е. количеству соединений.
Соединения можно брать из пула, пользоваться ими и после того как база уже не нужна, закрывать соединения

Зачем availableConnections и usedConnections?

Зачем proxy?
Избежать диких соединений, т.е. чтобы все соединения создавались в пуле, а не в другом месте, например в дао

Как используются соединения?
Создаются в пуле, передаются в нужный пакет и класс. Используются и возвращаются в пул
 */
public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();
   /*
   когда какой-то поток меняет флаг, этот флаг меняется за одно атомарное действие, и не кешируется, все потоки видят
   последнее значение
   кешируется - что значит? - т.е. потоки видят последнее действие

    private static final AtomicBoolean isPoolCreated = new AtomicBoolean(false);

    При запуске нового потока создается копия хранилища, которой пользуетя поток.
    В нашем случае: ? - создается пул соединений
    Изменения, произведенные в копии, могут не сразу находить отражение в основном хранилище, и наоборот. Для получения
    актуального значения следует прибегнуть к синхронизации.
    isPoolCreated - переменная обеспечивает гарантию видимости, видно последнее изменение, запись данных в переменную
    происходит перед каждым последующим чтением.
    Так же обеспечивается атомарность действия, составное действие делается атомарным (чтение-изменение-запись).
    Проходит одно атомарное действие, флаг меняется и все потоки видят последнее изменение.
    В нашем случае атомарное действие: ? Создание пула соединений? Т.е. определенное количество соединений? (POOL_SIZE)
    Atomic - более качественная оболочка над volotile
    Atomic - изменяемый тип, применятеся для многопоточности

    - атомарная переменная (AtomicBoolean isPoolCreated) - неблокирующая синхронизация
    Неблокирующая сихнорнизация - это значит, что данные, с которыми мы работаем не блокируются полностью, но изменяются
    достаточно быстро чтобы стало видно всем потокам.
     */
    private static final AtomicBoolean isPoolCreated = new AtomicBoolean(false);
    // ReentrantLock represents a replacement of the Synchronized mechanism
    // it is blocked/unlocked by lock/unlock methods by itself
    /*
    Lock locker = new ReentrantLock(true);

    потоки будут проходить лок, не случайным рандомом, как это у synchronizes или у lock без true,  а получат доступ к
    локу именно в том порядке в котором перед ним останавливались
    Все кто остановлен в ожидании, будут получать доступ в порядке подхода, в честном порядке (с параметром true)
    В общем случае synchronized никакого порядка не обеспечивает

    lock - блокирует прохождение через участок кода
    после lock работает только один поток, остальные потоки будут стоять и ждать пока поток не вызовет unlock
    Потоки в нашем случае - это соединения?
     */
    private static final Lock locker = new ReentrantLock(true); // for independent thread execution
    // размер пула - в нашем случае это 5 соединений?
    private static final int POOL_SIZE = 5;

    private static ConnectionPool instance; // должен быть один пул с 5ю соединениями
    // ?    BlockingDeque - для чего?
    // доступные и используемые соединения
    /*
    Блокирующая очередь BlockingDeque гарантирует остановку потока, запрашивающего элемент из пустой очереди до появления
    в ней элемента, доступного для извлечения, а также блокирующего поток, пытающийся вставить элемент в заполненную
    очередь до тех пор, пока в очереди не освободится позиция.
    В нашем случае: останавливаем поток запрашивающий connection из пустой очереди, до появления в ней connection-а
   */
    private final BlockingDeque<ProxyConnection> availableConnections;
    private final BlockingDeque<ProxyConnection> usedConnections;
    // конструктор, создает и размещает 5 соединений в переменную availableConnections - создает connection pool
    private ConnectionPool() {
        // для чего разделение на availableConnections и usedConnections?
        availableConnections = new LinkedBlockingDeque<>(POOL_SIZE);
        usedConnections = new LinkedBlockingDeque<>(POOL_SIZE);
        // создаем 5 соединений, размещаем в
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                ProxyConnection connection = ConnectionFactory.createConnection();
            // void put(E e) — добавляет элемент в очередь. Если очередь заполнена, то ожидает, пока освободится место;
                availableConnections.put(connection);
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
    // если ConnectionPool не создан, то поток блокируется, создается ConnectionPool и поток разблокируется
    public static ConnectionPool getInstance() {
        if (!isPoolCreated.get()) {
            try {
                locker.lock(); // locking
                if (instance == null) {
                    instance = new ConnectionPool();
                    isPoolCreated.set(true);
                }
            } finally {
                locker.unlock(); // unlocking
            }
        }
        return instance;
    }
    // размещаем connection в usedConnections
    // перейдет в usedConnection после того как воспользуемся connection-ом
    public Connection acquireConnection() {
        ProxyConnection connection = null;
        try {
        // E take() — извлекает и удаляет элемент из очереди. Если очередь пуста, то ожидает, пока там появится элемент;
            connection = availableConnections.take();
            // void put(E e) — добавляет элемент в очередь. Если очередь заполнена, то ожидает, пока освободится место;
            usedConnections.put(connection);
        } catch (InterruptedException e) {
            logger.error("failed to acquire available connection", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }
// если connection является ProxyConnection, соединение удаляется из usedConnections и помещяется в availableConnections
    public boolean releaseConnection(Connection connection) {
        if (!(connection instanceof ProxyConnection)) {
            logger.error("wrong instance");
            return false;
        }
        try {
            usedConnections.remove(connection);
            availableConnections.put((ProxyConnection) connection);
            return true;
        } catch (InterruptedException e) {
            logger.error("failed to release a connection", e);
            Thread.currentThread().interrupt();
        }
        return false;
    }
// уничтожение пула, извлекаем все соединения из availableConnections и закрываем по порядку
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
// По завершении работы приложения следует выгрузить или дерегистрировать драйвер
    private void deregisterDrivers() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("failed to deregister driver", e);
            }
        });
    }
}