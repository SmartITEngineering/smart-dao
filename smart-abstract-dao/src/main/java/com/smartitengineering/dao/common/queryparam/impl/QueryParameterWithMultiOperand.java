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

import com.smartitengineering.dao.common.queryparam.MultiOperandQueryParameter;
import com.smartitengineering.dao.common.queryparam.OperatorType;
import com.smartitengineering.dao.common.queryparam.ParameterType;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author imyousuf
 */
public class QueryParameterWithMultiOperand<Template extends Object>
    implements MultiOperandQueryParameter<Template> {
    
    private QueryParameterAdapter<Template> queryParameter = new QueryParameterAdapter<Template>();

    protected QueryParameterWithMultiOperand() {
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
                     Template... values) {
        queryParameter.setPropertyName(propertyName);
        queryParameter.setType(type);
        queryParameter.setOperatorType(operatorType);
        queryParameter.setValues(Arrays.asList(values));
        queryParameter.setInitialized(true);
    }

    public Collection<Template> getValues() {
        return queryParameter.getValues();
    }

}
