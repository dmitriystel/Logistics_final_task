package com.stelmashok.logistics.model.query;
// для чего класс?
public class QueryParametersHolder {
    public static final String SEARCH_QUERY = "search_query";
    public static final String CATEGORY = "selected_category";
    public static final String CITY = "selected_city";
    public static final String SORT_BY = "sort_by";
    public static final String PAGE = "page";
    public static final String ITEMS_PER_PAGE = "items_per_page";

    public static final String SORT_NEW = "new";
    public static final String SORT_OLD = "old";
    public static final String SORT_CHEAP = "cheap";
    public static final String SORT_EXPENSIVE = "expensive";

    private QueryParametersHolder() {
    }
}
