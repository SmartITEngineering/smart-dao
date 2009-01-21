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

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.search.CommonFreeTextPersistentDao;
import com.smartitengineering.dao.common.search.CommonFreeTextSearchDao;
import com.smartitengineering.dao.common.search.compass.CommonCompassSearchDao;
import com.smartitengineering.dao.common.search.compass.CompassReadExtender;
import com.smartitengineering.domain.PersistentDTO;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;

/**
 *
 * @author imyousuf
 */
public class CompassSearchDao<Template extends PersistentDTO>
    implements CommonFreeTextSearchDao<Template>,
               CommonFreeTextPersistentDao<Template>,
               CommonCompassSearchDao<Template> {

    private Compass compass;
    
    private CommonReadDao<Template> readDao;

    public Collection<Template> search(final List<QueryParameter> parameters) {
        return this.<Collection<Template>>customOperation(new CompassReadExtender<Collection<Template>>(){

            public Collection<Template> customSearch(CompassSession compassSession) {
                CompassQuery query = CompassQueryBuilder.getCompassQueryFromQueryParam(compassSession, parameters);
                CompassHits hits = query.hits();
                Set<Template> result = new LinkedHashSet<Template>();
                for(int i = 0; i < hits.length(); ++i) {
                    result.add((Template)hits.data(i));
                }
                return result;
            }
        });
    }

    public Collection<Template> search(QueryParameter... parameters) {
        return search(Arrays.asList(parameters));
    }

    public void save(final Template... data) {
        this.<Void>customOperation(new CompassReadExtender<Void>() {

            public Void customSearch(CompassSession compassSession) {
                CompassTransaction transaction =
                    compassSession.beginTransaction();
                try {
                    for (Template datum : data) {
                        compassSession.save(datum);
                    }
                    transaction.commit();
                }
                catch (Exception ex) {
                    transaction.rollback();
                }
                return null;
            }
        });
    }

    public void update(final Template... data) {
        this.<Void>customOperation(new CompassReadExtender<Void>() {

            public Void customSearch(CompassSession compassSession) {
                CompassTransaction transaction =
                    compassSession.beginTransaction();
                try {
                    for (Template datum : data) {
                        compassSession.delete(datum);
                        compassSession.save(datum);
                    }
                    transaction.commit();
                }
                catch (Exception ex) {
                    transaction.rollback();
                }
                return null;
            }
        });
    }

    public void delete(final Template... data) {
        this.<Void>customOperation(new CompassReadExtender<Void>() {

            public Void customSearch(CompassSession compassSession) {
                CompassTransaction transaction =
                    compassSession.beginTransaction();
                try {
                    for (Template datum : data) {
                        compassSession.save(datum);
                    }
                    transaction.commit();
                }
                catch (Exception ex) {
                    transaction.rollback();
                }
                return null;
            }
        });
    }

    public <Return> Return customOperation(CompassReadExtender<Return> extender)
        throws IllegalArgumentException {
        if (compass == null) {
            throw new IllegalStateException(
                "Compass search engine not initialized!");
        }
        if (extender == null) {
            throw new IllegalArgumentException();
        }
        Return searchResult;
        final CompassSession session = compass.openSession();
        if (session == null) {
            throw new IllegalStateException("Could not open session!");
        }
        try {
            searchResult = extender.customSearch(session);
        }
        catch (Exception ex) {
            searchResult = null;
        }
        finally {
            if (!session.isClosed()) {
                session.close();
            }
        }
        return searchResult;
    }

    public Compass getCompass() {
        return compass;
    }

    public void setCompass(Compass compass) {
        this.compass = compass;
    }

    public CommonReadDao<Template> getReadDao() {
        return readDao;
    }

    public void setReadDao(CommonReadDao<Template> readDao) {
        this.readDao = readDao;
    }
}
