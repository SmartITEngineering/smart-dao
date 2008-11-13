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

import java.io.Serializable;
import java.util.Hashtable;

public class QueryParameter<TemplateClass extends Object>
    implements Serializable {

    public static final Integer PARAMETER_TYPE_PROPERTY = 1;
    public static final Integer PARAMETER_TYPE_ORDER_BY = 2;
    public static final Integer PARAMETER_TYPE_MAX_RESULT = 3;
    public static final Integer PARAMETER_TYPE_FIRST_RESULT = 4;
    public static final Integer PARAMETER_TYPE_DISJUNCTION = 5;
    public static final Integer PARAMETER_TYPE_NESTED_PROPERTY = 6;
    public static final Integer PARAMETER_TYPE_COUNT = 7;
    public static final Integer PARAMETER_TYPE_ROW_COUNT = 8;
    public static final Integer PARAMETER_TYPE_SUM = 9;
    public static final Integer PARAMETER_TYPE_MAX = 10;
    public static final Integer PARAMETER_TYPE_MIN = 11;
    public static final Integer PARAMETER_TYPE_AVG = 12;
    public static final Integer PARAMETER_TYPE_GROUP_BY = 13;
    public static final Integer PARAMETER_TYPE_COUNT_DISTINCT = 15;
    public static final Integer PARAMETER_TYPE_DISTINCT_PROP = 16;
    public static final Integer PARAMETER_TYPE_UNIT_PROP = 17;
    public static final Integer PARAMETER_TYPE_PROP_LIST = 18;
    public static final Integer PARAMETER_TYPE_IN = 19;
    public static final Integer PARAMETER_TYPE_NOT_IN = 20;
    public static final Integer OPERATOR_EQUAL = 1;
    public static final Integer OPERATOR_LESSER_EQUAL = 2;
    public static final Integer OPERATOR_GREATER_EQUAL = 3;
    public static final Integer OPERATOR_LESSER = 4;
    public static final Integer OPERATOR_GREATER = 5;
    public static final Integer OPERATOR_NOT_EQUAL = 6;
    public static final Integer OPERATOR_IS_NULL = 7;
    public static final Integer OPERATOR_IS_NOT_NULL = 8;
    public static final Integer OPERATOR_STRING_LIKE = 9;
    public static final Integer OPERATOR_BETWEEN = 10;
    public static final Integer OPERATOR_IS_EMPTY = 11;
    public static final Integer OPERATOR_IS_NOT_EMPTY = 12;

    public QueryParameter(String propertyName,
                          Integer type,
                          Integer operator,
                          TemplateClass parameter) {
        if (propertyName == null || type == null || type.intValue() <= 0 ||
            operator == null || operator.intValue() <= 0 || parameter == null) {
            IllegalArgumentException exception = new IllegalArgumentException();
            throw exception;
        }
        this.type = type;
        this.operator = operator;
        this.parameter = parameter;
        this.setPropertyName(propertyName);
    }
    private String propertyName;
    private TemplateClass parameter = null;
    private TemplateClass parameter2 = null;
    private Integer type;
    private Hashtable<String, QueryParameter> nestedParameters;
    private Integer operator;
    private MatchMode matchMode;
    private FetchMode fetchMode;

    public TemplateClass getParameter()
        throws IllegalArgumentException {
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter Not SET");
        }
        return parameter;
    }

    /**
     * @param parameter
     *           The parameter to set.
     */
    public void setParameter(TemplateClass parameter) {
        if (parameter instanceof Serializable) {
            this.parameter = parameter;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return Returns the nestedParameters.
     */
    public Hashtable<String, QueryParameter> getNestedParameters() {
        return nestedParameters;
    }

    /**
     * @param nestedParameters
     *           The nestedParameters to set.
     */
    public void setNestedParameters(
        Hashtable<String, QueryParameter> nestedParameters) {
        this.nestedParameters = nestedParameters;
    }

    /**
     * @return Returns the type.
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     *           The type to set.
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return Returns the fetchMode.
     */
    public FetchMode getFetchMode() {
        return fetchMode;
    }

    /**
     * @param fetchMode
     *           The fetchMode to set.
     */
    public void setFetchMode(FetchMode fetchMode) {
        this.fetchMode = fetchMode;
    }

    /**
     * @return Returns the matchMode.
     */
    public MatchMode getMatchMode() {
        return matchMode;
    }

    /**
     * @param matchMode
     *           The matchMode to set.
     */
    public void setMatchMode(MatchMode matchMode) {
        this.matchMode = matchMode;
    }

    /**
     * @return Returns the operator.
     */
    public Integer getOperator() {
        return operator;
    }

    /**
     * @param operator
     *           The operator to set.
     */
    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    /**
     * @return Returns the parameter2.
     */
    public TemplateClass getParameter2() {
        return parameter2;
    }

    /**
     * @param parameter2 The parameter2 to set.
     */
    public void setParameter2(TemplateClass parameter2) {
        this.parameter2 = parameter2;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public enum FetchMode {

        DEFAULT, EAGER, JOIN, LAZY, SELECT;
    }

    public enum MatchMode {

        ANYWHERE, END, EXACT, START;
    }
    
    public enum Order {
        DESC, ASC;
    }
}
