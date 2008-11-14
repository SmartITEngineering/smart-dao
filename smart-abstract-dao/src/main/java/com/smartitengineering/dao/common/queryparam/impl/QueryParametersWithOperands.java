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
import com.smartitengineering.dao.common.queryparam.MultiOperandQueryParameter;
import com.smartitengineering.dao.common.queryparam.OperatorType;
import com.smartitengineering.dao.common.queryparam.ParameterType;
import com.smartitengineering.dao.common.queryparam.SimpleNameValueQueryParameter;
import com.smartitengineering.dao.common.queryparam.UniOperandQueryParameter;
import java.util.Arrays;

/**
 *
 * @author imyousuf
 */
public class QueryParametersWithOperands<Template extends Object>
    extends AbstractQueryParameter<Template>
    implements UniOperandQueryParameter<Template>,
               BiOperandQueryParameter<Template>,
               MultiOperandQueryParameter<Template>,
               SimpleNameValueQueryParameter<Template> {

    protected QueryParametersWithOperands() {
    }

    public void init(ParameterType type,
                     String propertyName,
                     OperatorType operatorType,
                     Template value) {
        setPropertyName(propertyName);
        setType(type);
        setOperatorType(operatorType);
        setValue(value);
        setInitialized(true);
    }

    public void init(ParameterType type,
                     String propertyName,
                     OperatorType operatorType,
                     Template firstValue,
                     Template secondValue) {
        setPropertyName(propertyName);
        setType(type);
        setOperatorType(operatorType);
        setValue(firstValue);
        setSecondValue(secondValue);
        setInitialized(true);
    }

    public void init(ParameterType type,
                     String propertyName,
                     OperatorType operatorType,
                     Template... values) {
        setPropertyName(propertyName);
        setType(type);
        setOperatorType(operatorType);
        setValues(Arrays.asList(values));
        setInitialized(true);
    }

    public void init(ParameterType type,
                     String propertyName,
                     Template value) {
        setPropertyName(propertyName);
        setType(type);
        setValue(value);
        setInitialized(true);
    }
}
