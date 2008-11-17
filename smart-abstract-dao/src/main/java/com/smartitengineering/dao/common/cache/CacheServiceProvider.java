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
package com.smartitengineering.dao.common.cache;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author imyousuf
 */
public interface CacheServiceProvider<Key, Value> {

    public void putToCache(Key pKey,
                           Value pValue);

    public void putToCache(Map<Key, Value> pKeyValueMap);

    public void putToCache(Key pKey,
                           Value pValue,
                           Date pExpiryDateTime);

    public void putToCache(Map<Key, Value> pKeyValueMap,
                           Date pExpiryDateTime);

    public Value expireFromCache(Key pKey);

    public Value retrieveFromCache(Key pKey);

    public Map<Key, Value> retrieveFromCache(List<Key> pKeys);

    public Map<Key, Value> retrieveFromCache(Key... pKeys);

    public boolean containsKey(Key pKey);

    public void setPrefixStrategy(CacheKeyPrefixStrategy pCacheKeyPrefixStrategy);

    public CacheKeyPrefixStrategy getPrefixStrategy();

    public Map getStats();

    public boolean clearCache();
}
