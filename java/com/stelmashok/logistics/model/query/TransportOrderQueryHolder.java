package com.stelmashok.logistics.model.query;

public class TransportOrderQueryHolder {
    public static final String SQL_WHERE = " WHERE ";
    public static final String SQL_AND = " AND ";
    public static final String SQL_ITEM_TITLE_LIKE = " items.title LIKE CONCAT('%', ? ,'%') ";
    public static final String SQL_ITEM_CITY_ID = " cities.id = ? ";
    public static final String SQL_ORDER_BY = " ORDER BY ";
    public static final String SQL_LIMIT = " LIMIT ?, ? ";
    public static final String SQL_LIMIT_DEFAULT = " LIMIT 0, 16 ";
    public static final String SQL_ITEM_CATEGORY_ID = " item_categories.id = ? ";
    public static final String SQL_UPDATE_TIME_DESC = " items.update_time DESC ";
    public static final String SQL_UPDATE_TIME_ASC = " items.update_time ASC ";
    public static final String SQL_PRICE_ASC = " items.price ASC ";
    public static final String SQL_PRICE_DESC = " items.price DESC ";

    private TransportOrderQueryHolder () {
    }
}
