/*
 * This is a common dao with basic CRUD operations and is not limited to any 
 * persistent layer implementation
 * 
 * Copyright (C) 2008  Imran M Yousuf (imyousuf@smartitengineering.com)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.smartitengineering.dao.common.queryparam;

import com.smartitengineering.dao.common.queryparam.impl.QueryParameterInstantiationFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author imyousuf
 */
public final class QueryParameterFactory {

    private QueryParameterFactory() {
        throw new AssertionError("Factory is not instantiable");
    }

    public static QueryParameter<Void> getIsEmptyCollectionPropertyParam(
        final String propertyName) {
        OperatorType operatorType = OperatorType.OPERATOR_IS_EMPTY;
        return getNoOperandQueryParameter(propertyName, operatorType);
    }

    public static QueryParameter<Void> getIsNotEmptyCollectionPropertyParam(
        final String propertyName) {
        OperatorType operatorType = OperatorType.OPERATOR_IS_NOT_EMPTY;
        return getNoOperandQueryParameter(propertyName, operatorType);
    }

    public static QueryParameter<Void> getIsNullPropertyParam(
        final String propertyName) {
        OperatorType operatorType = OperatorType.OPERATOR_IS_NULL;
        return getNoOperandQueryParameter(propertyName, operatorType);
    }

    public static QueryParameter<Void> getIsNotNullPropertyParam(
        final String propertyName) {
        OperatorType operatorType = OperatorType.OPERATOR_IS_NOT_NULL;
        return getNoOperandQueryParameter(propertyName, operatorType);
    }

    public static <Template> QueryParameter<Template> getEqualPropertyParam(
        final String propertyName,
        final Template parameter) {
        OperatorType operatorType = OperatorType.OPERATOR_EQUAL;
        return getUniOperandQueryParam(propertyName, operatorType, parameter);
    }

    public static <Template> QueryParameter<Template> getLesserThanPropertyParam(
        final String propertyName,
        final Template parameter) {
        OperatorType operatorType = OperatorType.OPERATOR_LESSER;
        return getUniOperandQueryParam(propertyName, operatorType, parameter);
    }

    public static <Template> QueryParameter<Template> getGreaterThanPropertyParam(
        final String propertyName,
        final Template parameter) {
        OperatorType operatorType = OperatorType.OPERATOR_GREATER;
        return getUniOperandQueryParam(propertyName, operatorType, parameter);
    }

    public static <Template> QueryParameter<Template> getLesserThanEqualToPropertyParam(
        final String propertyName,
        final Template parameter) {
        OperatorType operatorType = OperatorType.OPERATOR_LESSER_EQUAL;
        return getUniOperandQueryParam(propertyName, operatorType, parameter);
    }

    public static <Template> QueryParameter<Template> getGreaterThanEqualToPropertyParam(
        final String propertyName,
        final Template parameter) {
        OperatorType operatorType = OperatorType.OPERATOR_GREATER_EQUAL;
        return getUniOperandQueryParam(propertyName, operatorType, parameter);
    }

    public static <Template> QueryParameter<Template> getNotEqualPropertyParam(
        final String propertyName,
        final Template parameter) {
        OperatorType operatorType = OperatorType.OPERATOR_NOT_EQUAL;
        return getUniOperandQueryParam(propertyName, operatorType, parameter);
    }

    public static <Template> QueryParameter<String> getStringLikePropertyParam(
        final String propertyName,
        final Template parameter) {
        return getStringLikePropertyParam(propertyName, parameter,
            MatchMode.ANYWHERE);
    }

    public static <Template> QueryParameter<String> getStringLikePropertyParam(
        final String propertyName,
        final Template parameter,
        final MatchMode matchMode) {
        StringLikeQueryParameter queryParameter =
            QueryParameterInstantiationFactory.getStringLikeQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_PROPERTY, propertyName,
            parameter.toString(), matchMode);
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getBetweenPropertyParam(
        final String propertyName,
        final Template parameter,
        final Template secondParameter) {
        BiOperandQueryParameter<Template> queryParameter =
            QueryParameterInstantiationFactory.getBiOperandQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_PROPERTY, propertyName,
            OperatorType.OPERATOR_BETWEEN, parameter,
            secondParameter);
        return queryParameter;
    }

    public static <Template> QueryParameter<Template> getIsInPropertyParam(
        final String propertyName,
        final Template... parameters) {
        OperatorType operatorType = OperatorType.OPERATOR_IS_IN;
        return getMultiOperandQueryParameter(propertyName, operatorType,
            parameters);
    }

    public static <Template> QueryParameter<Template> getIsNotInPropertyParam(
        final String propertyName,
        final Template... parameters) {
        OperatorType operatorType = OperatorType.OPERATOR_IS_NOT_IN;
        return getMultiOperandQueryParameter(propertyName, operatorType,
            parameters);
    }

    public static QueryParameter<Order> getOrderByParam(
        final String propertyName,
        final Order order) {
        SimpleNameValueQueryParameter<Order> queryParameter =
            QueryParameterInstantiationFactory.<Order>
            getSimpleNameValueQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_ORDER_BY, propertyName,
            order);
        return queryParameter;
    }

    public static QueryParameter<Integer> getMaxResultsParam(
        final Integer maxResult) {
        ValueOnlyQueryParameter<Integer> queryParameter =
            QueryParameterInstantiationFactory.getValueOnlyQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_MAX_RESULT, maxResult);
        return queryParameter;
    }

    public static QueryParameter<Integer> getFirstResultParam(
        final Integer firstResult) {
        ValueOnlyQueryParameter<Integer> queryParameter =
            QueryParameterInstantiationFactory.getValueOnlyQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_FIRST_RESULT,
            firstResult);
        return queryParameter;
    }

    public static QueryParameter<Void> getDisjunctionParam(
        final QueryParameter... parameters) {
        BasicCompoundQueryParameter queryParameter =
            QueryParameterInstantiationFactory.getBasicCompoundQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_DISJUNCTION, parameters);
        return queryParameter;
    }

    public static QueryParameter<Void> getConjunctionParam(
        final QueryParameter... parameters) {
        BasicCompoundQueryParameter queryParameter =
            QueryParameterInstantiationFactory.getBasicCompoundQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_CONJUNCTION, parameters);
        return queryParameter;
    }

    public static QueryParameter<Void> getNestedParametersParam(
        final String propertyName,
        final FetchMode fetchMode,
        final QueryParameter... parameters) {
        CompositionQueryParameter queryParameter =
            QueryParameterInstantiationFactory.getCompositionQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_NESTED_PROPERTY,
            propertyName, fetchMode, parameters);
        return queryParameter;
    }

    public static QueryParameter<Void> getRowCountParam() {
        return getQueryParameterForProjection(
            ParameterType.PARAMETER_TYPE_ROW_COUNT, "a");
    }

    public static QueryParameter<Void> getElementCountParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            ParameterType.PARAMETER_TYPE_COUNT, propertyName);
    }

    public static QueryParameter<Void> getElementSumParam(
        final String propertyName) {
        return getQueryParameterForProjection(ParameterType.PARAMETER_TYPE_SUM,
            propertyName);
    }

    public static QueryParameter<Void> getElementMaxParam(
        final String propertyName) {
        return getQueryParameterForProjection(ParameterType.PARAMETER_TYPE_MAX,
            propertyName);
    }

    public static QueryParameter<Void> getElementMinParam(
        final String propertyName) {
        return getQueryParameterForProjection(ParameterType.PARAMETER_TYPE_MIN,
            propertyName);
    }

    public static QueryParameter<Void> getElementAvgParam(
        final String propertyName) {
        return getQueryParameterForProjection(ParameterType.PARAMETER_TYPE_AVG,
            propertyName);
    }

    public static QueryParameter<Void> getDistinctElementCountParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            ParameterType.PARAMETER_TYPE_COUNT_DISTINCT, propertyName);
    }

    public static QueryParameter<Void> getGroupByPropParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            ParameterType.PARAMETER_TYPE_GROUP_BY, propertyName);
    }

    public static QueryParameter<Void> getDistinctPropProjectionParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            ParameterType.PARAMETER_TYPE_DISTINCT_PROP, propertyName);
    }

    public static QueryParameter<Void> getPropProjectionParam(
        final String propertyName) {
        return getQueryParameterForProjection(
            ParameterType.PARAMETER_TYPE_UNIT_PROP, propertyName);
    }

    public static List<QueryParameter<Void>> getMultiPropProjectionParam(
        final String... propertyNames) {
        List<QueryParameter<Void>> params = new ArrayList<QueryParameter<Void>>(
            propertyNames.length);
        for (String propertyName : propertyNames) {
            params.add(getPropProjectionParam(propertyName));
        }
        return params;
    }

    public static <T> T getCastedQueryParameter(final Class<T> castClass,
                                                final Collection<ParameterType> parameterTypes,
                                                final Collection<OperatorType> operators,
                                                final QueryParameter parameter) {
        if (parameterTypes.contains(parameter.getParameterType())) {
            if (operators.isEmpty() ||
                (QueryParameterCastHelper.BI_OPERAND_PARAM_HELPER.isWithOperator(
                parameter) && operators.contains(
                QueryParameterCastHelper.BI_OPERAND_PARAM_HELPER.
                castToOperatorParam(parameter).getOperatorType()))) {
                return castClass.cast(parameter);
            }
        }
        throw new IllegalArgumentException("Parameter type not supported");
    }

    private static QueryParameter<Void> getQueryParameterForProjection(
        final ParameterType type,
        final String propertyName) {
        NameOnlyQueryParameter queryParameter =
            QueryParameterInstantiationFactory.getNameOnlyQueryParameter();
        queryParameter.init(type, propertyName);
        return queryParameter;
    }

    private static <Template> QueryParameter<Template> getUniOperandQueryParam(
        final String propertyName,
        OperatorType operatorType,
        final Template parameter) {
        UniOperandQueryParameter<Template> queryParameter =
            QueryParameterInstantiationFactory.getUniOperandQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_PROPERTY, propertyName,
            operatorType, parameter);
        return queryParameter;
    }

    private static QueryParameter<Void> getNoOperandQueryParameter(
        final String propertyName,
        OperatorType operatorType) {
        NoOperandQueryParamater queryParameter =
            QueryParameterInstantiationFactory.getNoOperandQueryParamater();
        queryParameter.init(ParameterType.PARAMETER_TYPE_PROPERTY, propertyName,
            operatorType);
        return queryParameter;
    }

    private static <Template> QueryParameter<Template> getMultiOperandQueryParameter(
        final String propertyName,
        OperatorType operatorType,
        final Template[] parameters) {
        MultiOperandQueryParameter<Template> queryParameter =
            QueryParameterInstantiationFactory.getMultiOperandQueryParameter();
        queryParameter.init(ParameterType.PARAMETER_TYPE_PROPERTY, propertyName,
            operatorType, parameters);
        return queryParameter;
    }
}
