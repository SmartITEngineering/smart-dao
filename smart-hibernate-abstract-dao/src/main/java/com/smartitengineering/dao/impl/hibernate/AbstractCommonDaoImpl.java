/*
 * This is a common dao with basic CRUD operations and is not limited to any 
 * persistent layer implementation
 * 
 * Copyright (C) 2008  Imran M Yousuf
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.smartitengineering.dao.impl.hibernate;

import com.smartitengineering.dao.common.CommonDao;
import com.smartitengineering.dao.common.QueryParameter;
import com.smartitengineering.domain.PersistentDTO;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Imran M Yousuf
 */
public abstract class AbstractCommonDaoImpl<Template extends PersistentDTO>
    extends AbstractDAO<Template>
    implements CommonDao<Template> {

    private Class<? extends Template> entityClass;

    public void save(Template... states) {
        if (states != null && states.length <= 0) {
            return;
        }
        if (entityClass == null) {
            entityClass = (Class<Template>) states[0].getClass();
        }
        createEntity(states);
    }

    public void update(Template... states) {
        if (states != null && states.length <= 0) {
            return;
        }
        if (entityClass == null) {
            entityClass = (Class<Template>) states[0].getClass();
        }
        updateEntity(states);
    }

    public void delete(Template... states) {
        if (states != null && states.length <= 0) {
            return;
        }
        if (entityClass == null) {
            entityClass = (Class<Template>) states[0].getClass();
        }
        deleteEntity(states);
    }

    public Set<Template> getAll() {
        return new HashSet<Template>(getList(Collections.<QueryParameter>
            emptyList()));
    }

    public Template getById(Integer id) {
        QueryParameter<Integer> param =
            new QueryParameter<Integer>(
            "id",
            QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_EQUAL,
            id);
        try {
            return getSingle(param);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Set<Template> getByIds(List<Integer> ids) {
        QueryParameter<Collection<Integer>> param =
            new QueryParameter<Collection<Integer>>(
            "id",
            QueryParameter.PARAMETER_TYPE_IN,
            QueryParameter.OPERATOR_EQUAL,
            ids);
        Collection<Template> result;
        try {
            result = getList(param);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            result = Collections.<Template>emptyList();
        }
        return new HashSet<Template>(result);
    }

    protected Class getEntityClass() {
        return entityClass;
    }

    protected void setEntityClass(Class<? extends Template> entityClass) {
        this.entityClass = entityClass;
    }

    public Template getSingle(List<QueryParameter> query) {
        return readSingle(entityClass, query);
    }

    public List<Template> getList(List<QueryParameter> query) {
        return readList(entityClass, query);
    }

    public Object getOther(List<QueryParameter> query) {
        return readOther(entityClass, query);
    }

    public Template getSingle(QueryParameter... query) {
        return readSingle(entityClass, query);
    }

    public List<Template> getList(QueryParameter... query) {
        return readList(entityClass, query);
    }

    public Object getOther(QueryParameter... query) {
        return readOther(entityClass, query);
    }

    public List<? extends Object> getOtherList(
        Hashtable<String, QueryParameter> query) {
        return readOtherList(entityClass, query);
    }

    public List<? extends Object> getOtherList(List<QueryParameter> query) {
        return readOtherList(entityClass, query);
    }

    public List<? extends Object> getOtherList(QueryParameter... query) {
        return readOtherList(entityClass, query);
    }
}
