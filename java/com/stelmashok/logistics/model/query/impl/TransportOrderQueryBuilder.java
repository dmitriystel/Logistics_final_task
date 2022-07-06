package com.stelmashok.logistics.model.query.impl;

import com.stelmashok.logistics.model.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Map;

public class TransportOrderQueryBuilder implements QueryBuilder {

    @Override
    public String buildSelectQuery(String sql, Map<String, String[]> parameters) {
        StringBuilder query = new StringBuilder(sql);
        if (!parameters.isEmpty()) {
            query.append(buildQueryFromSearchParameters(parameters));
            query.append(buildQueryFromSortParameters(parameters));
            query.append(buildQueryFromPaginationParameters(parameters));
        }
        return query.toString();
    }

    @Override
    public String buildCountQuery(String sql, Map<String, String[]> parameters) {
        StringBuilder query = new StringBuilder(sql);
        if (!parameters.isEmpty()) {
            query.append(buildQueryFromSearchParameters(parameters));
            query.append(buildQueryFromSortParameters(parameters));
        }
        return query.toString();
    }

    @Override
    public ArrayList<Object> extractSelectQueryArguments(Map<String, String[]> parameters) {
        ArrayList<Object> arguments = new ArrayList<>();
        if (parameters.containsKey(SEARCH_QUERY)) {
            arguments.add(parameters.get(SEARCH_QUERY)[0]);
        }
        if (parameters.containsKey(CATEGORY)) {
            arguments.add(parameters.get(CATEGORY)[0]);
        }
        if (parameters.containsKey(CITY)) {
            arguments.add(parameters.get(CITY)[0]);
        }
        if (parameters.containsKey(PAGE) && parameters.containsKey(ITEMS_PER_PAGE)) {
            int currentPage = Integer.parseInt(parameters.get(PAGE)[0]);
            int recordsPerPage = Integer.parseInt(parameters.get(ITEMS_PER_PAGE)[0]);
            int startItem = currentPage * recordsPerPage - recordsPerPage;
            arguments.add(startItem);
            arguments.add(recordsPerPage);
        }
        return arguments;
    }

    @Override
    public ArrayList<Object> extractCountQueryArguments(Map<String, String[]> parameters) {
        ArrayList<Object> arguments = new ArrayList<>();
        if (parameters.containsKey(SEARCH_QUERY)) {
            arguments.add(parameters.get(SEARCH_QUERY)[0]);
        }
        if (parameters.containsKey(CATEGORY)) {
            arguments.add(parameters.get(CATEGORY)[0]);
        }
        if (parameters.containsKey(CITY)) {
            arguments.add(parameters.get(CITY)[0]);
        }
        return arguments;
    }

    private String buildQueryFromSearchParameters(Map<String, String[]> parameters) {
        if (!parameters.containsKey(SEARCH_QUERY) && !parameters.containsKey(CATEGORY) && !parameters.containsKey(CITY)) {
            return EMPTY_STRING;
        }
        StringBuilder query = new StringBuilder(SQL_WHERE);
        boolean isNotEmpty = false;
        if (parameters.containsKey(SEARCH_QUERY)) {
            query.append(SQL_ITEM_TITLE_LIKE);
            isNotEmpty = true;
        }
        if (parameters.containsKey(CATEGORY)) {
            if (isNotEmpty) {
                query.append(SQL_AND);
            }
            query.append(SQL_ITEM_CATEGORY_ID);
            isNotEmpty = true;
        }
        if (parameters.containsKey(CITY)) {
            if (isNotEmpty) {
                query.append(SQL_AND);
            }
            query.append(SQL_ITEM_CITY_ID);
            isNotEmpty = true;
        }
        return query.toString();
    }

    private String buildQueryFromSortParameters(Map<String, String[]> parameters) {
        StringBuilder query = new StringBuilder(EMPTY_STRING);
        if (parameters.containsKey(SORT_BY)) {
            query.append(SQL_ORDER_BY);
            switch (parameters.get(SORT_BY)[0]) {
                case SORT_OLD -> {
                    query.append(SQL_UPDATE_TIME_ASC);
                }
                case SORT_CHEAP -> {
                    query.append(SQL_PRICE_ASC);
                }
                case SORT_EXPENSIVE -> {
                    query.append(SQL_PRICE_DESC);
                }
                default -> query.append(SQL_UPDATE_TIME_DESC);
            }
        } else {
            query.append(SQL_ORDER_BY);
            query.append(SQL_UPDATE_TIME_DESC);
        }
        return query.toString();
    }

    private String buildQueryFromPaginationParameters(Map<String, String[]> parameters) {
        StringBuilder query = new StringBuilder(EMPTY_STRING);
        if (parameters.containsKey(PAGE) && parameters.containsKey(ITEMS_PER_PAGE)) {
            query.append(SQL_LIMIT);
        } else {
            query.append(SQL_LIMIT_DEFAULT);
        }
        return query.toString();
    }
}
