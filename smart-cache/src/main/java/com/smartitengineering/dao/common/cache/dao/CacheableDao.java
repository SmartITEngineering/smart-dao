/*
 * This is a common dao with basic CRUD operations and is not limited to any
 * persistent layer implementation
 *
 * Copyright (C) 2011  Imran M Yousuf (imyousuf@smartitengineering.com)
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
package com.smartitengineering.dao.common.cache.dao;

import com.smartitengineering.dao.common.CommonDao;
import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.cache.CacheServiceProvider;
import com.smartitengineering.domain.PersistentDTO;
import java.io.Serializable;

/**
 *
 * @author imyousuf
 */
public interface CacheableDao<Template extends PersistentDTO, IdType extends Serializable, CacheKey extends Serializable>
    extends CommonDao<Template, IdType> {

  CacheKeyGenearator<Template, IdType, CacheKey> getCacheKeyGenearator();

  void setCacheKeyGenearator(CacheKeyGenearator<Template, IdType, CacheKey> cacheKeyGenearator);

  CacheServiceProvider<CacheKey, Template> getCacheProvider();

  void setCacheProvider(CacheServiceProvider<CacheKey, Template> cacheProvider);

  CommonReadDao<Template, IdType> getPrimaryReadDao();

  void setPrimaryReadDao(CommonReadDao<Template, IdType> primaryReadDao);

  CommonWriteDao<Template> getPrimaryWriteDao();

  void setPrimaryWriteDao(CommonWriteDao<Template> primaryWriteDao);
}
