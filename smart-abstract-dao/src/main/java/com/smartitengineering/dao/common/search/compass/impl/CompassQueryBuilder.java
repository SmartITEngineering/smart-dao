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

import com.smartitengineering.dao.common.queryparam.QueryParameter;
import java.util.List;
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
                booleanQueryBuilder.addMust(getCompassQuery(parameter, session));
            }
            return booleanQueryBuilder.toQuery();
        }
        else {
            return getCompassQuery(parameters.get(0), session);
        }
    }

    private static CompassQuery getCompassQuery(final QueryParameter param,
                                                final CompassSession session) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
