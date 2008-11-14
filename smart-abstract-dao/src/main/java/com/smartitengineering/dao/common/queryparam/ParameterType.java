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

/**
 *
 * @author imyousuf
 */
public enum ParameterType {

    PARAMETER_TYPE_PROPERTY,
    PARAMETER_TYPE_ORDER_BY,
    PARAMETER_TYPE_MAX_RESULT,
    PARAMETER_TYPE_FIRST_RESULT,
    PARAMETER_TYPE_CONJUNCTION,
    PARAMETER_TYPE_DISJUNCTION,
    PARAMETER_TYPE_NESTED_PROPERTY,
    PARAMETER_TYPE_COUNT,
    PARAMETER_TYPE_ROW_COUNT,
    PARAMETER_TYPE_SUM,
    PARAMETER_TYPE_MAX,
    PARAMETER_TYPE_MIN,
    PARAMETER_TYPE_AVG,
    PARAMETER_TYPE_GROUP_BY,
    PARAMETER_TYPE_COUNT_DISTINCT,
    PARAMETER_TYPE_DISTINCT_PROP,
    PARAMETER_TYPE_UNIT_PROP,
}
