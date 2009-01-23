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

import java.util.Collections;

/**
 *
 * @author imyousuf
 */
public abstract class AbstractQueryParameterCastHelper<P extends QueryParameter>
    implements QueryParameterCastHelper<P> {
    
    private boolean hasOperator;
    
    protected AbstractQueryParameterCastHelper(boolean hasOperator) {
        this.hasOperator = hasOperator;
    }

    protected AbstractQueryParameterCastHelper() {
        this(false);
    }

    public P cast(QueryParameter parameter)
        throws IllegalArgumentException {
        return (P) QueryParameterFactory.getCastedQueryParameter(
            getEndTypeClass(), getParameterTypes(), hasOperator() ? getOperators()
            : Collections.EMPTY_SET, parameter);
    }

    public QueryParameterWithOperator castToOperatorParam(
        QueryParameter parameter) {
        if(!isWithOperator(parameter)) {
            throw new IllegalArgumentException("Parameter does not support operators!");
        }
        return QueryParameterWithOperator.class.cast(parameter);
    }

    public boolean isWithOperator(QueryParameter parameter) {
        return parameter != null &&
            parameter instanceof QueryParameterWithOperator;
    }

    public boolean hasOperator() {
        return hasOperator;
    }

    public abstract Class<P> getEndTypeClass();
}
