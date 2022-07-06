package com.stelmashok.logistics.model.query;

import java.util.ArrayList;
import java.util.Map;

public interface QueryBuilder {
    String buildSelectQuery(String sql, Map<String, String[]> parameters);

    String buildCountQuery(String sql, Map<String, String[]> parameters);

    ArrayList<Object> extractSelectQueryArguments(Map<String, String[]> parameters);

    ArrayList<Object> extractCountQueryArguments(Map<String, String[]> parameters);
}
