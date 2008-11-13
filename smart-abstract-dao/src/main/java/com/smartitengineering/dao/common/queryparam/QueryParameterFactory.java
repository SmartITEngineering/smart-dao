/*
 * This is a common dao with basic CRUD operations and is not limited to any 
 * persistent layer implementation
 * 
 * Copyright (C) 2008  Imran M Yousuf
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.smartitengineering.dao.common.queryparam;

import com.smartitengineering.dao.common.QueryParameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author imyousuf
 */
public final class QueryParameterFactory {

    private QueryParameterFactory() {
        throw new AssertionError("Factory is not instantiable");
    }

    public static QueryParameter<String> getIsEmptyCollectionPropertyParam(
        final String propertyName) {
        QueryParameter<String> queryParameter = new QueryParameter<String>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_IS_EMPTY, "");
        return queryParameter;
    }

    public static QueryParameter<String> getIsNotEmptyCollectionPropertyParam(
        final String propertyName) {
        QueryParameter<String> queryParameter = new QueryParameter<String>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_IS_NOT_EMPTY, "");
        return queryParameter;
    }

    public static QueryParameter<String> getIsNullPropertyParam(
        final String propertyName) {
        QueryParameter<String> queryParameter = new QueryParameter<String>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_IS_NULL, "");
        return queryParameter;
    }

    public static QueryParameter<String> getIsNotNullPropertyParam(
        final String propertyName) {
        QueryParameter<String> queryParameter = new QueryParameter<String>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_IS_NOT_NULL, "");
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getEqualPropertyParam(
        final String propertyName,
        final Template parameter) {
        QueryParameter<Template> queryParameter = new QueryParameter<Template>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_EQUAL, parameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getLesserThanPropertyParam(
        final String propertyName,
        final Template parameter) {
        QueryParameter<Template> queryParameter = new QueryParameter<Template>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_LESSER, parameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getGreaterThanPropertyParam(
        final String propertyName,
        final Template parameter) {
        QueryParameter<Template> queryParameter = new QueryParameter<Template>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_GREATER, parameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getLesserThanEqualToPropertyParam(
        final String propertyName,
        final Template parameter) {
        QueryParameter<Template> queryParameter = new QueryParameter<Template>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_LESSER_EQUAL, parameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getGreaterThanEqualToPropertyParam(
        final String propertyName,
        final Template parameter) {
        QueryParameter<Template> queryParameter = new QueryParameter<Template>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_GREATER_EQUAL, parameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getNotEqualPropertyParam(
        final String propertyName,
        final Template parameter) {
        QueryParameter<Template> queryParameter = new QueryParameter<Template>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_NOT_EQUAL, parameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<String> getStringLikePropertyParam(
        final String propertyName,
        final Template parameter) {
        return getStringLikePropertyParam(propertyName, parameter,
            QueryParameter.MatchMode.ANYWHERE);
    }

    public static <Template> QueryParameter<String> getStringLikePropertyParam(
        final String propertyName,
        final Template parameter,
        final QueryParameter.MatchMode matchMode) {
        QueryParameter<String> queryParameter = new QueryParameter<String>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_STRING_LIKE, parameter.toString());
        queryParameter.setMatchMode(matchMode);
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getBetweenPropertyParam(
        final String propertyName,
        final Template parameter,
        final Template secondParameter) {
        QueryParameter<Template> queryParameter = new QueryParameter<Template>(
            propertyName, QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_BETWEEN, parameter);
        queryParameter.setParameter2(secondParameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<Collection<Template>> getIsInPropertyParam(
        final String propertyName,
        final Template... parameters) {
        List<Template> parameter = Arrays.asList(parameters);
        QueryParameter<Collection<Template>> queryParameter =
            new QueryParameter<Collection<Template>>(propertyName,
            QueryParameter.PARAMETER_TYPE_IN, QueryParameter.OPERATOR_EQUAL,
            parameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<Collection<Template>> getIsNotInPropertyParam(
        final String propertyName,
        final Template... parameters) {
        List<Template> parameter = Arrays.asList(parameters);
        QueryParameter<Collection<Template>> queryParameter =
            new QueryParameter<Collection<Template>>(propertyName,
            QueryParameter.PARAMETER_TYPE_NOT_IN, QueryParameter.OPERATOR_EQUAL,
            parameter);
        return queryParameter;
    }

    public static QueryParameter<QueryParameter.Order> getOrderByParam(
        final String propertyName,
        final QueryParameter.Order order) {
        QueryParameter<QueryParameter.Order> queryParameter =
            new QueryParameter<QueryParameter.Order>(propertyName,
            QueryParameter.PARAMETER_TYPE_ORDER_BY,
            QueryParameter.OPERATOR_EQUAL, order);
        return queryParameter;
    }

    public static QueryParameter<Integer> getMaxResultsParam(
        final Integer maxResult) {
        QueryParameter<Integer> queryParameter =
            new QueryParameter<Integer>("a",
            QueryParameter.PARAMETER_TYPE_MAX_RESULT,
            QueryParameter.OPERATOR_EQUAL, maxResult);
        return queryParameter;
    }

    public static QueryParameter<Integer> getFirstResultParam(
        final Integer maxResult) {
        QueryParameter<Integer> queryParameter =
            new QueryParameter<Integer>("a",
            QueryParameter.PARAMETER_TYPE_FIRST_RESULT,
            QueryParameter.OPERATOR_EQUAL, maxResult);
        return queryParameter;
    }

    public static QueryParameter<String> getDisjunctionParam(
        final QueryParameter... parameters) {
        QueryParameter<String> queryParameter = new QueryParameter<String>("b",
            QueryParameter.PARAMETER_TYPE_DISJUNCTION,
            QueryParameter.OPERATOR_EQUAL, "");
        queryParameter.setNestedParameters(getQueryParamHashtable(parameters));
        return queryParameter;
    }

    public static QueryParameter<String> getNestedParametersParam(
        final String propertyName,
        final QueryParameter.FetchMode fetchMode,
        final QueryParameter... parameters) {
        QueryParameter<String> queryParameter = new QueryParameter<String>(
            propertyName, QueryParameter.PARAMETER_TYPE_NESTED_PROPERTY,
            QueryParameter.OPERATOR_EQUAL, "");
        queryParameter.setNestedParameters(getQueryParamHashtable(parameters));
        queryParameter.setFetchMode(fetchMode);
        return queryParameter;
    }

    public static QueryParameter<String> getRowCountParam() {
        return getQueryParameterForProjection(
            QueryParameter.PARAMETER_TYPE_ROW_COUNT, "a");
    }

    public static QueryParameter<String> getElementCountParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            QueryParameter.PARAMETER_TYPE_COUNT, propertyName);
    }

    public static QueryParameter<String> getElementSumParam(
        final String propertyName) {
        return getQueryParameterForProjection(QueryParameter.PARAMETER_TYPE_SUM,
            propertyName);
    }

    public static QueryParameter<String> getElementMaxParam(
        final String propertyName) {
        return getQueryParameterForProjection(QueryParameter.PARAMETER_TYPE_MAX,
            propertyName);
    }

    public static QueryParameter<String> getElementMinParam(
        final String propertyName) {
        return getQueryParameterForProjection(QueryParameter.PARAMETER_TYPE_MIN,
            propertyName);
    }

    public static QueryParameter<String> getElementAvgParam(
        final String propertyName) {
        return getQueryParameterForProjection(QueryParameter.PARAMETER_TYPE_AVG,
            propertyName);
    }

    public static QueryParameter<String> getDistinctElementCountParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            QueryParameter.PARAMETER_TYPE_COUNT_DISTINCT, propertyName);
    }

    public static QueryParameter<String> getGroupByPropParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            QueryParameter.PARAMETER_TYPE_GROUP_BY, propertyName);
    }

    public static QueryParameter<String> getDistinctPropProjectionParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            QueryParameter.PARAMETER_TYPE_DISTINCT_PROP, propertyName);
    }

    public static QueryParameter<String> getPropProjectionParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            QueryParameter.PARAMETER_TYPE_UNIT_PROP, propertyName);
    }

    public static QueryParameter<List<String>> getMultiPropProjectionParam(
        final String... propertyNames) {
        QueryParameter<List<String>> queryParameter =
            new QueryParameter<List<String>>("a",
            QueryParameter.PARAMETER_TYPE_PROP_LIST,
            QueryParameter.OPERATOR_EQUAL, Arrays.asList(propertyNames));
        return queryParameter;
    }

    private static QueryParameter<String> getQueryParameterForProjection(
        final Integer type,
        final String propertyName) {
        QueryParameter<String> queryParameter = new QueryParameter<String>(
            propertyName, type, QueryParameter.OPERATOR_EQUAL, "");
        return queryParameter;
    }

    private static Hashtable<String, QueryParameter> getQueryParamHashtable(
        final QueryParameter... params) {
        Hashtable<String, QueryParameter> table =
            new Hashtable<String, QueryParameter>();
        for (QueryParameter parameter : params) {
            String paramName = parameter.getPropertyName();
            if (table.containsKey(paramName)) {
                int i = 1;
                while (table.containsKey(new StringBuilder(paramName).append(
                    i).toString())) {
                    i++;
                }
                paramName = new StringBuilder(paramName).append(i).toString();
            }
            table.put(paramName, parameter);
        }
        return table;
    }
}
