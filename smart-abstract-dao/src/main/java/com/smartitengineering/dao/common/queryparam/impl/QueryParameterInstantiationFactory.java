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
import com.smartitengineering.dao.common.queryparam.BiOperandQueryParameter;
import com.smartitengineering.dao.common.queryparam.CompositionQueryParameter;
import com.smartitengineering.dao.common.queryparam.MultiOperandQueryParameter;
import com.smartitengineering.dao.common.queryparam.NameOnlyQueryParameter;
import com.smartitengineering.dao.common.queryparam.NoOperandQueryParamater;
import com.smartitengineering.dao.common.queryparam.SimpleNameValueQueryParameter;
import com.smartitengineering.dao.common.queryparam.StringLikeQueryParameter;
import com.smartitengineering.dao.common.queryparam.UniOperandQueryParameter;
import com.smartitengineering.dao.common.queryparam.ValueOnlyQueryParameter;

/**
 *
 * @author imyousuf
 */
public final class QueryParameterInstantiationFactory {

    private QueryParameterInstantiationFactory() {
        throw new AssertionError();
    }

    public static BasicCompoundQueryParameter getBasicCompoundQueryParameter() {
        return new CompoundQueryParameter();
    }

    public static CompositionQueryParameter getCompositionQueryParameter() {
        return new CompoundQueryParameter();
    }
    
    public static NoOperandQueryParamater getNoOperandQueryParamater() {
        return new NameQueryParameter();
    }

    public static <Template> UniOperandQueryParameter<Template> getUniOperandQueryParameter() {
        return new QueryParametersWithOperands<Template>();
    }

    public static StringLikeQueryParameter getStringLikeQueryParameter() {
        return new QueryParameterForStringOperand();
    }

    public static <Template> BiOperandQueryParameter<Template> getBiOperandQueryParameter() {
        return new QueryParametersWithOperands<Template>();
    }

    public static <Template> MultiOperandQueryParameter<Template> getMultiOperandQueryParameter() {
        return new QueryParametersWithOperands<Template>();
    }

    public static NameOnlyQueryParameter getNameOnlyQueryParameter() {
        return new NameQueryParameter();
    }

    public static <Template> ValueOnlyQueryParameter<Template> getValueOnlyQueryParameter() {
        return new OnlyValueQueryParameter<Template>();
    }

    public static <Template> SimpleNameValueQueryParameter<Template> getSimpleNameValueQueryParameter() {
        return new QueryParametersWithOperands<Template>();
    }
}
