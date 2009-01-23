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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * This interface will basically help you undertand the top-level type of the
 * query parameter and cast it. Its intended so that end user can easily
 * determine how to use them.
 * @author imyousuf
 */
public interface QueryParameterCastHelper<T extends QueryParameter> {

    /**
     * Returns the parameter types of the helper
     * @return Type of the parameters it supports
     */
    public Collection<ParameterType> getParameterTypes();

    /**
     * Checks whether the parameter is a param with operator or not
     * @param parameter The parameter to check
     * @return True if the parameter has operators
     */
    public boolean isWithOperator(QueryParameter parameter);

    /**
     * Casts the parameter to parameter with operator.
     * @param parameter The parameter to cast
     * @return Casted version of the parameter
     * @throws IllegalArgumentException If isWithOperator(parameter) return false
     */
    public QueryParameterWithOperator castToOperatorParam(
        QueryParameter parameter)
        throws IllegalArgumentException;

    /**
     * Returns whether the cast helper supports any particular operator(s)
     * @return true if the helper needs param to have certain operator(s)
     */
    public boolean hasOperator();

    /**
     * Returns the supported operator types
     * @return Supported operator types
     * @throws UnsupportedOperationException If hasOperator returns false
     */
    public Collection<OperatorType> getOperators()
        throws UnsupportedOperationException;

    /**
     * Cast and return the parameter to the top level QueryParameter descendent
     * if and only if the helper supports the parameter and operator
     * @param parameter QueryParameter to be casted
     * @return top level cast of parameter
     * @throws IllegalArgumentException If parameter and operator of
     *                                  QueryParameter is not supported by this
     *                                  helper
     */
    public T cast(final QueryParameter parameter)
        throws IllegalArgumentException;
    /**
     * Use this constant for casting query parameters of type
     * PARAMETER_TYPE_NESTED_PROPERTY
     */
    public static final QueryParameterCastHelper<CompositionQueryParameter> COMPOSITION_PARAM_FOR_NESTED_TYPE = new AbstractQueryParameterCastHelper<CompositionQueryParameter>() {

        public Collection<ParameterType> getParameterTypes() {
            return Collections.singleton(
                ParameterType.PARAMETER_TYPE_NESTED_PROPERTY);
        }

        public Collection<OperatorType> getOperators() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Class<CompositionQueryParameter> getEndTypeClass() {
            return CompositionQueryParameter.class;
        }
    };
    /**
     * Use this constant to cast parameters of type PARAMETER_TYPE_PROPERTY and
     * operators any of - OPERATOR_IS_EMPTY, OPERATOR_IS_NOT_EMPTY,
     * OPERATOR_IS_NULL, OPERATOR_IS_NOT_NULL
     */
    public static final QueryParameterCastHelper<NoOperandQueryParamater> NO_OPERAND_PARAM_HELPER = new AbstractQueryParameterCastHelper<NoOperandQueryParamater>(true) {

        public Collection<ParameterType> getParameterTypes() {
            return Collections.singleton(ParameterType.PARAMETER_TYPE_PROPERTY);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            return Arrays.asList(OperatorType.OPERATOR_IS_EMPTY,
                OperatorType.OPERATOR_IS_NOT_EMPTY,
                OperatorType.OPERATOR_IS_NULL, OperatorType.OPERATOR_IS_NOT_NULL);
        }

        @Override
        public Class<NoOperandQueryParamater> getEndTypeClass() {
            return NoOperandQueryParamater.class;
        }
    };
    /**
     * Use this constant for type - PARAMETER_TYPE_PROPERTY and operators -
     * OperatorType.OPERATOR_EQUAL, OperatorType.OPERATOR_GREATER,
     * OperatorType.OPERATOR_GREATER_EQUAL, OperatorType.OPERATOR_LESSER,
     * OperatorType.OPERATOR_LESSER_EQUAL, OperatorType.OPERATOR_NOT_EQUAL
     */
    public static final QueryParameterCastHelper<UniOperandQueryParameter> UNI_OPERAND_PARAM_HELPER = new AbstractQueryParameterCastHelper<UniOperandQueryParameter>(true) {

        @Override
        public Class<UniOperandQueryParameter> getEndTypeClass() {
            return UniOperandQueryParameter.class;
        }

        public Collection<ParameterType> getParameterTypes() {
            return Collections.singleton(ParameterType.PARAMETER_TYPE_PROPERTY);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            return Arrays.asList(OperatorType.OPERATOR_EQUAL,
                OperatorType.OPERATOR_GREATER,
                OperatorType.OPERATOR_GREATER_EQUAL,
                OperatorType.OPERATOR_LESSER, OperatorType.OPERATOR_LESSER_EQUAL,
                OperatorType.OPERATOR_NOT_EQUAL);
        }
    };
    /**
     * Use this constant for type - PARAMETER_TYPE_PROPERTY and operator -
     * OPERATOR_STRING_LIKE
     */
    public static final QueryParameterCastHelper<StringLikeQueryParameter> STRING_PARAM_HELPER = new AbstractQueryParameterCastHelper<StringLikeQueryParameter>(true) {

        @Override
        public Class<StringLikeQueryParameter> getEndTypeClass() {
            return StringLikeQueryParameter.class;
        }

        public Collection<ParameterType> getParameterTypes() {
            return Collections.singleton(ParameterType.PARAMETER_TYPE_PROPERTY);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            return Collections.singleton(OperatorType.OPERATOR_STRING_LIKE);
        }
    };
    /**
     * Use this constant for type - PARAMETER_TYPE_PROPERTY and operator(s) -
     * OPERATOR_BETWEEN
     */
    public static final QueryParameterCastHelper<BiOperandQueryParameter> BI_OPERAND_PARAM_HELPER = new AbstractQueryParameterCastHelper<BiOperandQueryParameter>(true) {

        @Override
        public Class<BiOperandQueryParameter> getEndTypeClass() {
            return BiOperandQueryParameter.class;
        }

        public Collection<ParameterType> getParameterTypes() {
            return Collections.singleton(ParameterType.PARAMETER_TYPE_PROPERTY);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            return Collections.singleton(OperatorType.OPERATOR_BETWEEN);
        }
    };
    /**
     * Use this constant for type - PARAMETER_TYPE_PROPERTY and operator(s) -
     * OPERATOR_IS_IN, OPERATOR_IS_NOT_IN
     */
    public static final QueryParameterCastHelper<MultiOperandQueryParameter> MULTI_OPERAND_PARAM_HELPER = new AbstractQueryParameterCastHelper<MultiOperandQueryParameter>(true) {

        @Override
        public Class<MultiOperandQueryParameter> getEndTypeClass() {
            return MultiOperandQueryParameter.class;
        }

        public Collection<ParameterType> getParameterTypes() {
            return Collections.singleton(ParameterType.PARAMETER_TYPE_PROPERTY);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            return Arrays.asList(OperatorType.OPERATOR_IS_IN,
                OperatorType.OPERATOR_IS_NOT_IN);
        }
    };
    /**
     * Use this constant for type - PARAMETER_TYPE_ORDER_BY
     */
    public static final QueryParameterCastHelper<SimpleNameValueQueryParameter> SIMPLE_PARAM_HELPER = new AbstractQueryParameterCastHelper<SimpleNameValueQueryParameter>() {

        @Override
        public Class<SimpleNameValueQueryParameter> getEndTypeClass() {
            return SimpleNameValueQueryParameter.class;
        }

        public Collection<ParameterType> getParameterTypes() {
            return Collections.singleton(ParameterType.PARAMETER_TYPE_ORDER_BY);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    /**
     * Use this constant for type - PARAMETER_TYPE_MAX_RESULT, 
     * PARAMETER_TYPE_FIRST_RESULT
     */
    public static final QueryParameterCastHelper<ValueOnlyQueryParameter> VALUE_PARAM_HELPER = new AbstractQueryParameterCastHelper<ValueOnlyQueryParameter>() {

        @Override
        public Class<ValueOnlyQueryParameter> getEndTypeClass() {
            return ValueOnlyQueryParameter.class;
        }

        public Collection<ParameterType> getParameterTypes() {
            return Arrays.asList(ParameterType.PARAMETER_TYPE_MAX_RESULT,
                ParameterType.PARAMETER_TYPE_FIRST_RESULT);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    /**
     * Use this constant for type - PARAMETER_TYPE_DISJUNCTION, 
     * PARAMETER_TYPE_CONJUNCTION
     */
    public static final QueryParameterCastHelper<BasicCompoundQueryParameter> BASIC_COMPOUND_PARAM_HELPER = new AbstractQueryParameterCastHelper<BasicCompoundQueryParameter>() {

        @Override
        public Class<BasicCompoundQueryParameter> getEndTypeClass() {
            return BasicCompoundQueryParameter.class;
        }

        public Collection<ParameterType> getParameterTypes() {
            return Arrays.asList(ParameterType.PARAMETER_TYPE_CONJUNCTION,
                ParameterType.PARAMETER_TYPE_DISJUNCTION);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    /**
     * Use this constant for type - ParameterType.PARAMETER_TYPE_COUNT, 
     * ParameterType.PARAMETER_TYPE_COUNT_DISTINCT, 
     * ParameterType.PARAMETER_TYPE_DISTINCT_PROP, 
     * ParameterType.PARAMETER_TYPE_MAX, 
     * ParameterType.PARAMETER_TYPE_MIN, 
     * ParameterType.PARAMETER_TYPE_ROW_COUNT, 
     * ParameterType.PARAMETER_TYPE_UNIT_PROP, 
     * ParameterType.PARAMETER_TYPE_SUM, 
     * ParameterType.PARAMETER_TYPE_AVG, 
     * ParameterType.PARAMETER_TYPE_GROUP_BY
     */
    public static final QueryParameterCastHelper<NameOnlyQueryParameter> NAME_ONLY_PARAM_HELPER = new AbstractQueryParameterCastHelper<NameOnlyQueryParameter>() {

        @Override
        public Class<NameOnlyQueryParameter> getEndTypeClass() {
            return NameOnlyQueryParameter.class;
        }

        public Collection<ParameterType> getParameterTypes() {
            return Arrays.asList(ParameterType.PARAMETER_TYPE_COUNT,
                ParameterType.PARAMETER_TYPE_COUNT_DISTINCT,
                ParameterType.PARAMETER_TYPE_DISTINCT_PROP,
                ParameterType.PARAMETER_TYPE_MAX,
                ParameterType.PARAMETER_TYPE_MIN,
                ParameterType.PARAMETER_TYPE_ROW_COUNT,
                ParameterType.PARAMETER_TYPE_UNIT_PROP,
                ParameterType.PARAMETER_TYPE_SUM,
                ParameterType.PARAMETER_TYPE_AVG,
                ParameterType.PARAMETER_TYPE_GROUP_BY);
        }

        public Collection<OperatorType> getOperators()
            throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
}
