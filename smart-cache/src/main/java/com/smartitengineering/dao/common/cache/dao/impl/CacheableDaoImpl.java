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
package com.smartitengineering.dao.common.cache.dao.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.cache.CacheServiceProvider;
import com.smartitengineering.dao.common.cache.Lock;
import com.smartitengineering.dao.common.cache.Mutex;
import com.smartitengineering.dao.common.cache.dao.CacheKeyGenearator;
import com.smartitengineering.dao.common.cache.dao.CacheableDao;
import com.smartitengineering.dao.common.cache.impl.CacheAPIFactory;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.domain.PersistentDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class CacheableDaoImpl<Template extends PersistentDTO, IdType extends Serializable, CacheKey extends Serializable>
    implements
    CacheableDao<Template, IdType, CacheKey> {

  @Inject
  @Named("primaryCacheableWriteDao")
  private CommonWriteDao<Template> primaryWriteDao;
  @Inject
  @Named("primaryCacheableReadDao")
  private CommonReadDao<Template, IdType> primaryReadDao;
  @Inject
  private CacheServiceProvider<CacheKey, Template> cacheProvider;
  @Inject
  private CacheKeyGenearator<Template, IdType, CacheKey> cacheKeyGenearator;
  protected transient final Logger logger = LoggerFactory.getLogger(getClass());
  protected final Mutex<CacheKey> mutex = CacheAPIFactory.<CacheKey>getMutex();

  @Override
  public CacheKeyGenearator<Template, IdType, CacheKey> getCacheKeyGenearator() {
    return cacheKeyGenearator;
  }

  @Override
  public void setCacheKeyGenearator(CacheKeyGenearator<Template, IdType, CacheKey> cacheKeyGenearator) {
    this.cacheKeyGenearator = cacheKeyGenearator;
  }

  @Override
  public CacheServiceProvider<CacheKey, Template> getCacheProvider() {
    return cacheProvider;
  }

  @Override
  public void setCacheProvider(CacheServiceProvider<CacheKey, Template> cacheProvider) {
    this.cacheProvider = cacheProvider;
  }

  @Override
  public CommonReadDao<Template, IdType> getPrimaryReadDao() {
    return primaryReadDao;
  }

  @Override
  public void setPrimaryReadDao(CommonReadDao<Template, IdType> primaryReadDao) {
    this.primaryReadDao = primaryReadDao;
  }

  @Override
  public CommonWriteDao<Template> getPrimaryWriteDao() {
    return primaryWriteDao;
  }

  @Override
  public void setPrimaryWriteDao(CommonWriteDao<Template> primaryWriteDao) {
    this.primaryWriteDao = primaryWriteDao;
  }

  @Override
  public Set<Template> getAll() {
    return primaryReadDao.getAll();
  }

  @Override
  public Set<Template> getByIds(List<IdType> pids) {
    if (pids == null || pids.isEmpty()) {
      return Collections.emptySet();
    }
    List<CacheKey> keys = new ArrayList<CacheKey>(pids.size());
    Map<CacheKey, IdType> keyIds = new HashMap<CacheKey, IdType>(keys.size());
    for (IdType pid : pids) {
      CacheKey key = cacheKeyGenearator.generateKeyFromId(pid);
      if (key == null) {
        continue;
      }
      keys.add(key);
      keyIds.put(key, pid);
    }
    if (keys == null || keys.isEmpty()) {
      return Collections.emptySet();
    }
    Map<CacheKey, Template> results = new HashMap<CacheKey, Template>(keys.size());
    List<CacheKey> missedKeys = new ArrayList<CacheKey>(keys);
    results.putAll(cacheProvider.retrieveFromCache(missedKeys));
    for (CacheKey id : results.keySet()) {
      missedKeys.remove(id);
    }
    Map<CacheKey, Lock<CacheKey>> locks = new HashMap<CacheKey, Lock<CacheKey>>(missedKeys.size());
    for (CacheKey missedId : missedKeys) {
      boolean attained = false;
      while (!attained) {
        try {
          locks.put(missedId, mutex.acquire(missedId));
          attained = true;
        }
        catch (Exception ex) {
          logger.warn("Could not acquire lock for user!");
        }
      }
    }
    results.putAll(cacheProvider.retrieveFromCache(missedKeys));
    for (CacheKey id : results.keySet()) {
      if (missedKeys.remove(id)) {
        mutex.release(locks.get(id));
      }
    }
    List<IdType> missedIds = new ArrayList<IdType>();
    for (CacheKey missedKey : missedKeys) {
      missedIds.add(keyIds.get(missedKey));
    }
    Set<Template> fromSource = primaryReadDao.getByIds(missedIds);
    for (Template template : fromSource) {
      final CacheKey key = cacheKeyGenearator.generateKeyFromObject(template);
      putToCache(template, key);
      results.put(key, template);
    }
    for (CacheKey id : missedKeys) {
      mutex.release(locks.get(id));
    }
    LinkedHashSet<Template> resultSet = new LinkedHashSet<Template>(results.size());
    for (CacheKey id : keys) {
      Template user = results.get(id);
      if (user != null) {
        resultSet.add(user);
      }
    }
    return resultSet;
  }

  @Override
  public Template getById(IdType id) {
    CacheKey key = cacheKeyGenearator.generateKeyFromId(id);
    if (key == null) {
      return primaryReadDao.getById(id);
    }
    Template template = cacheProvider.retrieveFromCache(key);
    if (template != null) {
      return template;
    }
    else {
      try {
        Lock<CacheKey> lock = mutex.acquire(key);
        template = cacheProvider.retrieveFromCache(key);
        if (template != null) {
          return template;
        }
        template = primaryReadDao.getById(id);
        if (template != null) {
          putToCache(template, key);
        }
        mutex.release(lock);
      }
      catch (Exception ex) {
        logger.warn("Could not do cache lookup!", ex);
      }
      return template;
    }
  }

  @Override
  public Template getSingle(List<QueryParameter> query) {
    return primaryReadDao.getSingle(query);
  }

  @Override
  public List<Template> getList(List<QueryParameter> query) {
    return primaryReadDao.getList(query);
  }

  @Override
  public <OtherTemplate> OtherTemplate getOther(List<QueryParameter> query) {
    return primaryReadDao.<OtherTemplate>getOther(query);
  }

  @Override
  public <OtherTemplate> List<OtherTemplate> getOtherList(List<QueryParameter> query) {
    return primaryReadDao.<OtherTemplate>getOtherList(query);
  }

  @Override
  public Template getSingle(QueryParameter... query) {
    return primaryReadDao.getSingle(query);
  }

  @Override
  public List<Template> getList(QueryParameter... query) {
    return primaryReadDao.getList(query);
  }

  @Override
  public <OtherTemplate> OtherTemplate getOther(QueryParameter... query) {
    return primaryReadDao.<OtherTemplate>getOther(query);
  }

  @Override
  public <OtherTemplate> List<OtherTemplate> getOtherList(QueryParameter... query) {
    return primaryReadDao.<OtherTemplate>getOtherList(query);
  }

  @Override
  public void save(Template... states) {
    primaryWriteDao.save(states);
  }

  @Override
  public void update(Template... states) {
    try {
      primaryWriteDao.update(states);
      for (Template template : states) {
        final CacheKey key = cacheKeyGenearator.generateKeyFromObject(template);
        if (key == null) {
          continue;
        }
        expireFromCache(key);
      }
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus did not invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public void delete(Template... states) {
    try {
      primaryWriteDao.delete(states);
      for (Template template : states) {
        final CacheKey key = cacheKeyGenearator.generateKeyFromObject(template);
        if (key == null) {
          continue;
        }
        expireFromCache(key);
      }
    }
    catch (RuntimeException exception) {
      logger.info("Could not delete thus did not invalidate cache!", exception);
      throw exception;
    }
  }

  protected void putToCache(Template template, CacheKey key) {
    cacheProvider.putToCache(key, template);
  }

  protected void expireFromCache(CacheKey key) {
    if (cacheProvider.containsKey(key)) {
      cacheProvider.expireFromCache(key);
    }
  }
}
