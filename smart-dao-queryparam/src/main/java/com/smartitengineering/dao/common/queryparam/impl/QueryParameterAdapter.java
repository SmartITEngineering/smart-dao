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

import com.smartitengineering.dao.common.queryparam.CompoundQueryParameter;
import com.smartitengineering.dao.common.queryparam.OperatorType;
import com.smartitengineering.dao.common.queryparam.ParameterType;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterWith2Values;
import com.smartitengineering.dao.common.queryparam.QueryParameterWithOperator;
import com.smartitengineering.dao.common.queryparam.QueryParameterWithPropertyName;
import com.smartitengineering.dao.common.queryparam.QueryParameterWithValue;
import com.smartitengineering.dao.common.queryparam.QueryParameterWithValues;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author imyousuf
 */
public class QueryParameterAdapter<Template extends Object>
    implements QueryParameter<Template>,
               QueryParameterWithPropertyName<Template>,
               QueryParameterWithOperator<Template>,
               QueryParameterWithValue<Template>,
               QueryParameterWith2Values<Template>,
               QueryParameterWithValues<Template>,
               CompoundQueryParameter<Template> {

    private boolean initialized;
    private ParameterType type;
    private String propertyName;
    private OperatorType operatorType;
    private Template value;
    private Template secondValue;
    private Collection<Template> values;
    private Collection<QueryParameter> nestedParameters;

    public QueryParameterAdapter() {
        initialized = false;
    }

    public ParameterType getParameterType() {
        return type;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public Template getValue() {
        return value;
    }

    public Template getFirstValue() {
        return value;
    }

    public Template getSecondValue() {
        return secondValue;
    }

    public Collection<Template> getValues() {
        if(values == null) {
            return Collections.<Template>emptyList();
        }
        return values;
    }

    public Collection<QueryParameter> getNestedParameters() {
        if(nestedParameters == null) {
            return Collections.<QueryParameter>emptyList();
        }
        return nestedParameters;
    }

    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    protected void setType(ParameterType type) {
        this.type = type;
    }

    protected void setNestedParameters(Collection<QueryParameter> nestedParameters) {
        this.nestedParameters = nestedParameters;
    }

    protected void setOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    protected void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    protected void setSecondValue(Template secondValue) {
        this.secondValue = secondValue;
    }

    protected void setValue(Template value) {
        this.value = value;
    }
    
    protected void setValues(Collection<Template> values) {
        this.values = values;
    }
}
