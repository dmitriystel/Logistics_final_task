package com.stelmashok.logistics.model.mapper;

// the class stores the names of the columns of tables from the database in constants
public class DatabaseColumnTitleHolder {
    public static final String USER_ID = "users.user_id";
    public static final String USER_CUSTOMER_NAME = "users.customer_name";
    public static final String USER_LOGIN = "users.login";
    public static final String USER_EMAIL = "users.email";
    public static final String USER_PASSWORD = "users.password";
    public static final String USER_NAME = "users.name";
    public static final String USER_SURNAME = "users.surname";
    public static final String USER_PHONE = "users.phone";
    public static final String USER_STATUS = "user_statuses.title";
    public static final String USER_ROLE = "user_roles.title";

    public static final String TRANSPORT_ORDER_ID = "transport_orders.order_id";
    public static final String TRANSPORT_USER = "users.customer_name";
    public static final String TRANSPORT_ORDER_DATE = "transport_orders.order_date";
    public static final String TRANSPORT_ORDER_DELIVERY_DATE = "transport_orders.delivery_date";
    public static final String TRANSPORT_ORDER_PRODUCT = "products.title";
    public static final String TRANSPORT_ORDER_UNLOADING = "unloadings.city";
    public static final String TRANSPORT_ORDER_CARRIER = "carriers.carrier_name";

    public static final String PRODUCT_ID = "products.product_id";
    public static final String PRODUCT_TITLE = "products.title";
    public static final String PRODUCT_WEIGHT = "products.weight";
    public static final String PRODUCT_DESCRIPTION = "products.description";

    public static final String UNLOADING_ID = "unloadings.unloading_id";
    public static final String UNLOADING_COUNTRY = "unloadings.country";
    public static final String UNLOADING_CITY = "unloadings.city";

    public static final String CARRIER_ID = "carriers.carrier_id";
    public static final String CARRIER_NAME = "carriers.carrier_name";
    public static final String CARRIER_TRUCK_NUMBER = "carriers.truck_number";

    private DatabaseColumnTitleHolder() {
    }
}
