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
package com.smartitengineering.dao.common.queryparam.impl;

import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.ParameterType;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author imyousuf
 */
public class CompositionQueryParameter
    implements com.smartitengineering.dao.common.queryparam.CompositionQueryParameter {

    private FetchMode fetchMode;
    private QueryParameterAdapter<Void> queryParameter =
        new QueryParameterAdapter<Void>();

    protected CompositionQueryParameter() {
    }

    public void init(ParameterType type,
                     String propertyName,
                     QueryParameter... parameters) {
        init(type, propertyName, FetchMode.DEFAULT, parameters);
    }

    public void init(ParameterType type,
                     String propertyName,
                     FetchMode fetchMode,
                     QueryParameter... parameters) {
        setFetchMode(fetchMode);
        queryParameter.setType(type);
        queryParameter.setPropertyName(propertyName);
        queryParameter.setNestedParameters(Arrays.asList(parameters));
        queryParameter.setInitialized(true);
    }

    public FetchMode getFetchMode() {
        return fetchMode;
    }

    protected void setFetchMode(FetchMode fetchMode) {
        this.fetchMode = fetchMode;
    }

    public Collection<QueryParameter> getNestedParameters() {
        return queryParameter.getNestedParameters();
    }

    public ParameterType getParameterType() {
        return queryParameter.getParameterType();
    }

    public boolean isInitialized() {
        return queryParameter.isInitialized();
    }

    public String getPropertyName() {
        return queryParameter.getPropertyName();
    }
}
