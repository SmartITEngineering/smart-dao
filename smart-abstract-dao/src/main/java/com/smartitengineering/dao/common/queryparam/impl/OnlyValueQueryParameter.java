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

import com.smartitengineering.dao.common.queryparam.ValueOnlyQueryParameter;
import com.smartitengineering.dao.common.queryparam.ParameterType;

/**
 *
 * @author imyousuf
 */
public class OnlyValueQueryParameter<Template extends Object>
    extends AbstractQueryParameter<Template>
    implements ValueOnlyQueryParameter<Template> {

    protected OnlyValueQueryParameter() {
    }

    public void init(ParameterType type,
                     Template value) {
        setType(type);
        setValue(value);
        setInitialized(true);
    }
}
