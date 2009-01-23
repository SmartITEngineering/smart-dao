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
package com.smartitengineering.dao.common.search.compass.impl;

import com.smartitengineering.dao.common.queryparam.BiOperandQueryParameter;
import com.smartitengineering.dao.common.queryparam.CompositionQueryParameter;
import com.smartitengineering.dao.common.queryparam.CompoundQueryParameter;
import com.smartitengineering.dao.common.queryparam.MultiOperandQueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterCastHelper;
import com.smartitengineering.dao.common.queryparam.QueryParameterWithOperator;
import com.smartitengineering.dao.common.queryparam.UniOperandQueryParameter;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder.CompassBooleanQueryBuilder;
import org.compass.core.CompassSession;

/**
 *
 * @author imyousuf
 */
public abstract class CompassQueryBuilder {

    private CompassQueryBuilder() {
        throw new AssertionError();
    }

    public static CompassQuery getCompassQueryFromQueryParam(
        final CompassSession session,
        final List<QueryParameter> parameters)
        throws IllegalArgumentException {
        if (session == null || parameters == null || parameters.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (parameters.size() > 1) {
            CompassBooleanQueryBuilder booleanQueryBuilder = session.
                queryBuilder().
                bool();
            for (QueryParameter parameter : parameters) {
                booleanQueryBuilder.addShould(
                    getCompassQuery(parameter, session, null));
            }
            return booleanQueryBuilder.toQuery();
        }
        else {
            return getCompassQuery(parameters.get(0), session, null);
        }
    }

    private static CompassQuery getCompassQuery(final QueryParameter param,
                                                final CompassSession session,
                                                final String context) {
        boolean conjuction = true;
        switch (param.getParameterType()) {
            case PARAMETER_TYPE_CONJUNCTION:
                return getCompoundQuery(session, (CompoundQueryParameter) param,
                    conjuction, context);
            case PARAMETER_TYPE_NESTED_PROPERTY:
                CompositionQueryParameter compositionQueryParameter =
                    QueryParameterCastHelper.COMPOSITION_PARAM_FOR_NESTED_TYPE.
                    cast(param);
                StringBuilder newContext = new StringBuilder();
                if (context != null) {
                    newContext.append(context);
                    newContext.append('.');
                }
                newContext.append(compositionQueryParameter.getPropertyName());
                return getCompoundQuery(session, (CompoundQueryParameter) param,
                    conjuction, newContext.toString());
            case PARAMETER_TYPE_DISJUNCTION:
                conjuction = false;
                return getCompoundQuery(session, (CompoundQueryParameter) param,
                    conjuction, context);
            case PARAMETER_TYPE_PROPERTY:
                return getSimplePropertyCompassQuery(param, session, context);
            default:
                throw new IllegalStateException("Unsupported query parameter!");
        }
    }

    private static CompassQuery getSimplePropertyCompassQuery(
        final QueryParameter parameter,
        final CompassSession session,
        final String context) {
        QueryParameterWithOperator withOperator =
            (QueryParameterWithOperator) parameter;
        switch (withOperator.getOperatorType()) {
            case OPERATOR_BETWEEN:
                BiOperandQueryParameter biOperandQueryParameter =
                    QueryParameterCastHelper.BI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().between(getPropertyName(
                    context, biOperandQueryParameter.getPropertyName()),
                    biOperandQueryParameter.getFirstValue(),
                    biOperandQueryParameter.getSecondValue(), true);
            case OPERATOR_EQUAL:
                UniOperandQueryParameter queryParameter =
                    QueryParameterCastHelper.UNI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().term(getPropertyName(context,
                    queryParameter.getPropertyName()), queryParameter.getValue());
            case OPERATOR_LESSER:
                UniOperandQueryParameter ltQueryParameter =
                    QueryParameterCastHelper.UNI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().lt(getPropertyName(context,
                    ltQueryParameter.getPropertyName()), ltQueryParameter.
                    getValue());
            case OPERATOR_LESSER_EQUAL:
                UniOperandQueryParameter leQueryParameter =
                    QueryParameterCastHelper.UNI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().le(getPropertyName(context,
                    leQueryParameter.getPropertyName()), leQueryParameter.
                    getValue());
            case OPERATOR_GREATER:
                UniOperandQueryParameter gtQueryParameter =
                    QueryParameterCastHelper.UNI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().gt(getPropertyName(context,
                    gtQueryParameter.getPropertyName()), gtQueryParameter.
                    getValue());
            case OPERATOR_GREATER_EQUAL:
                UniOperandQueryParameter geQueryParameter =
                    QueryParameterCastHelper.UNI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().ge(getPropertyName(context,
                    geQueryParameter.getPropertyName()), geQueryParameter.
                    getValue());
            case OPERATOR_NOT_EQUAL:
                UniOperandQueryParameter nQueryParameter =
                    QueryParameterCastHelper.UNI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().bool().addMustNot(session.
                    queryBuilder().term(getPropertyName(context,
                    nQueryParameter.getPropertyName()),
                    nQueryParameter.getValue())).toQuery();
            case OPERATOR_IS_IN:
                MultiOperandQueryParameter moqp =
                    QueryParameterCastHelper.MULTI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().multiPhrase(getPropertyName(
                    context, moqp.getPropertyName())).add(moqp.getValues().
                    toArray()).toQuery();
            case OPERATOR_IS_NOT_IN:
                MultiOperandQueryParameter nmoqp =
                    QueryParameterCastHelper.MULTI_OPERAND_PARAM_HELPER.cast(
                    parameter);
                return session.queryBuilder().bool().addMustNot(
                    session.queryBuilder().multiPhrase(getPropertyName(context,
                    nmoqp.getPropertyName())).add(nmoqp.getValues().toArray()).
                    toQuery()).toQuery();
            default:
                throw new IllegalStateException("Unknown Operator!");
        }
    }

    private static CompassQuery getCompoundQuery(CompassSession session,
                                                 CompoundQueryParameter param,
                                                 boolean conjuction,
                                                 String context) {
        CompassBooleanQueryBuilder queryBuilder = session.queryBuilder().bool();
        Collection<QueryParameter> parameters = param.getNestedParameters();
        for (QueryParameter parameter : parameters) {
            if (conjuction) {
                queryBuilder.addMust(
                    getCompassQuery(parameter, session, context));
            }
            else {
                queryBuilder.addShould(getCompassQuery(parameter, session,
                    context));
            }
        }
        return queryBuilder.toQuery();
    }

    private static String getPropertyName(String context,
                                          String propertyName) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(context)) {
            builder.append(context).append('.');
        }
        builder.append(propertyName);
        return builder.toString();
    }
}
