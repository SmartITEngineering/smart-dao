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
package com.smartitengineering.dao.impl.hibernate;

import com.smartitengineering.dao.common.CommonDao;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.domain.PersistentDTO;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        QueryParameter<Integer> param = QueryParameterFactory.<Integer>
            getEqualPropertyParam("id", id);
        try {
            return getSingle(param);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Set<Template> getByIds(List<Integer> ids) {
        QueryParameter<Integer> param = QueryParameterFactory.<Integer>
            getIsInPropertyParam("id", ids.toArray(new Integer[0]));
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

    public void setEntityClassString(String className) {
        try {
          this.entityClass =
          (Class<? extends Template>) Class.forName(className);
        }
        catch (ClassNotFoundException ex) {
          ex.printStackTrace();
        }
    }

    public Template getSingle(List<QueryParameter> query) {
        return readSingle(entityClass, query);
    }

    public List<Template> getList(List<QueryParameter> query) {
        return readList(entityClass, query);
    }

    public <OtherTemplate extends Object> OtherTemplate getOther(
        List<QueryParameter> query) {
        return this.<OtherTemplate>readOther(entityClass, query);
    }

    public Template getSingle(QueryParameter... query) {
        return readSingle(entityClass, query);
    }

    public List<Template> getList(QueryParameter... query) {
        return readList(entityClass, query);
    }

    public <OtherTemplate extends Object> OtherTemplate getOther(
        QueryParameter... query) {
        return this.<OtherTemplate>readOther(entityClass, query);
    }

    public <OtherTemplate extends Object> List<OtherTemplate> getOtherList(
        List<QueryParameter> query) {
        return readOtherList(entityClass, query);
    }

    public <OtherTemplate extends Object> List<OtherTemplate> getOtherList(
        QueryParameter... query) {
        return readOtherList(entityClass, query);
    }
}
