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

import com.smartitengineering.dao.common.queryparam.BasicCompoundQueryParameter;
import com.smartitengineering.dao.common.queryparam.CompositionQueryParameter;
import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.ParameterType;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import java.util.Arrays;

/**
 *
 * @author imyousuf
 */
public class CompoundQueryParameter
    extends AbstractQueryParameter<Void>
    implements BasicCompoundQueryParameter,
               CompositionQueryParameter {

    private FetchMode fetchMode;

    protected CompoundQueryParameter() {
    }

    public void init(ParameterType type,
                     QueryParameter... parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        setType(type);
        setPropertyName(propertyName);
        setNestedParameters(Arrays.asList(parameters));
        setInitialized(true);
    }

    public FetchMode getFetchMode() {
        return fetchMode;
    }

    protected void setFetchMode(FetchMode fetchMode) {
        this.fetchMode = fetchMode;
    }
}
