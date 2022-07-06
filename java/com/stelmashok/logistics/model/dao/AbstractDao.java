package com.stelmashok.logistics.model.dao;

import com.stelmashok.logistics.model.entity.AbstractEntity;
import com.stelmashok.logistics.exception.DaoException;

import java.sql.Connection;
import java.util.Optional;
/*
DAO - прослойка между приложением и СУБД.

- DAO инкапсулирует и абстрагирует бизнес-сущности системы и отображает их на реляционные данные в БД и обратно.
- DAO определяет общие способы использования соединения с БД, моменты его открытия и закрытия или извлечения
и возвращения в пул.
- В общем случае DAO определяет только стандартные CRUD-методы.

Вершина иерархии DAO представляет собой абстрактный класс с описанием набора методов, которые будут
использоваться при взаимодействии с группой таблиц. Как правило, это методы выбора, поиска сущности
по признаку, добавление, удаление и замена информации. Все абстрактные методы в своей сигнатуре содержат
throws DaoException, генерация которого будет происходить в случае возникновения SQLException, которое, в свою
очередь, не может быть обработано в слое DAO и передается на уровень вне DAO, чтобы вызывающий метод был
уведомлен об ошибке, и уже разработчик принимал решение о реакции на это исключение.

Также есть интерфейсы с перечисленными методами по каждому классу-сущности, с последующей реализацией.

Абстрактный класс обозначает общие во всех классах методы, которые будет реализованы во всех классах-наследниках
пакета
 */
// a set of methods that will be used when interacting with a group of tables
public abstract class AbstractDao<T extends AbstractEntity> {

    protected Connection connection;    //  для чего? Почему protected? Для доступа к полям класса Connection

    public void setConnection(Connection connection) {
        this.connection = connection;
    }    //  для чего?

    public abstract boolean create(T t) throws DaoException;

    public abstract Optional<T> findById(long id) throws DaoException;

    public abstract Optional<T> update(T t) throws DaoException;

    public abstract boolean delete(T t) throws DaoException;

    public abstract boolean delete(long id) throws DaoException;
}
