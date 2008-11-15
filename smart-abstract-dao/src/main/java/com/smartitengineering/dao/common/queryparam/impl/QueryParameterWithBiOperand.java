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

import com.smartitengineering.dao.common.queryparam.BiOperandQueryParameter;
import com.smartitengineering.dao.common.queryparam.OperatorType;
import com.smartitengineering.dao.common.queryparam.ParameterType;

/**
 *
 * @author imyousuf
 */
public class QueryParameterWithBiOperand<Template extends Object>
    implements BiOperandQueryParameter<Template> {
    
    private QueryParameterAdapter<Template> queryParameter = new QueryParameterAdapter<Template>();

    protected QueryParameterWithBiOperand() {
    }

    public ParameterType getParameterType() {
        return queryParameter.getParameterType();
    }

    public boolean isInitialized() {
        return queryParameter.isInitialized();
    }

    public OperatorType getOperatorType() {
        return queryParameter.getOperatorType();
    }

    public String getPropertyName() {
        return queryParameter.getPropertyName();
    }

    public void init(ParameterType type,
                     String propertyName,
                     OperatorType operatorType,
                     Template firstValue,
                     Template secondValue) {
        queryParameter.setType(type);
        queryParameter.setValue(firstValue);
        queryParameter.setSecondValue(secondValue);
        queryParameter.setPropertyName(propertyName);
        queryParameter.setOperatorType(operatorType);
        queryParameter.setInitialized(true);
    }

    public Template getFirstValue() {
        return queryParameter.getFirstValue();
    }

    public Template getSecondValue() {
        return queryParameter.getSecondValue();
    }

}
