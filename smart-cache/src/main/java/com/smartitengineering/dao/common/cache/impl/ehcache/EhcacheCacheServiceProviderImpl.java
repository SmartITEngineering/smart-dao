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
package com.smartitengineering.dao.common.cache.impl.ehcache;

import com.smartitengineering.dao.common.cache.CacheKeyPrefixStrategy;
import com.smartitengineering.dao.common.cache.CacheServiceProvider;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Statistics;

/**
 *
 * @author imyousuf
 */
public class EhcacheCacheServiceProviderImpl<Key, Value>
    implements CacheServiceProvider<Key, Value> {

  private static final char DEFAULT_SEPARATOR = ':';
  private CacheKeyPrefixStrategy prefixStrategy;
  private Cache mCache;

  @Override
  public void putToCache(Key key,
                         Value value) {
    if (key == null || value == null) {
      return;
    }
    try {
      String keyStr = calculateKey(key);
      mCache.put(new Element((Object) keyStr, (Object) value));
    }
    catch (Exception ex) {
    }
  }

  @Override
  public void putToCache(Map<Key, Value> keyValueMap) {
    Set<Map.Entry<Key, Value>> entrySet = keyValueMap.entrySet();
    for (Map.Entry<Key, Value> entry : entrySet) {
      putToCache(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void putToCache(Key key,
                         Value value,
                         Date expiryDateTime) {
    if (key == null || value == null || expiryDateTime == null) {
      return;
    }
    try {
      String keyStr = calculateKey(key);
      Element element = new Element((Object) keyStr, (Object) value);
      long milliSeconds = expiryDateTime.getTime() - System.currentTimeMillis();
      if (milliSeconds > 1000) {
        int ttlInSeconds = (int) (milliSeconds / 1000);
        element.setTimeToLive(ttlInSeconds);
      }
      mCache.put(element);
    }
    catch (Exception ex) {
    }
  }

  @Override
  public void putToCache(Map<Key, Value> keyValueMap,
                         Date expiryDateTime) {
    Set<Map.Entry<Key, Value>> entrySet = keyValueMap.entrySet();
    for (Map.Entry<Key, Value> entry : entrySet) {
      putToCache(entry.getKey(), entry.getValue(), expiryDateTime);
    }
  }

  @Override
  public Value expireFromCache(Key key) {
    if (key == null) {
      return null;
    }
    String keyStr = calculateKey(key);
    try {
      Value eValue = retrieveFromCache(key);
      mCache.remove(keyStr);
      return eValue;
    }
    catch (Exception ex) {
    }
    return null;
  }

  @Override
  public Value retrieveFromCache(Key key) {
    if (key == null) {
      return null;
    }
    String keyStr = calculateKey(key);
    try {
      final Element element = mCache.get(keyStr);
      if (element != null) {
        Value eValue = (Value) element.getObjectValue();
        return eValue;
      }
    }
    catch (Exception ex) {
    }
    return null;

  }

  @Override
  public Map<Key, Value> retrieveFromCache(List<Key> keys) {
    Map<Key, Value> retrievedValues = new HashMap<Key, Value>();
    try {
      for (Key eKey : keys) {
        Value value = retrieveFromCache(eKey);
        if (value != null) {
          retrievedValues.put(eKey, value);
        }
      }
    }
    catch (Exception exception) {
    }
    return retrievedValues;
  }

  @Override
  public Map<Key, Value> retrieveFromCache(Key... keys) {
    return retrieveFromCache(Arrays.asList(keys));
  }

  @Override
  public boolean containsKey(Key pKey) {
    try {
      String eKey = calculateKey(pKey);
      boolean eKeyExists = mCache.isKeyInCache(eKey);
      return eKeyExists;
    }
    catch (Exception ex) {
    }
    return false;
  }

  @Override
  public void setPrefixStrategy(CacheKeyPrefixStrategy cacheKeyPrefixStrategy) {
    if (cacheKeyPrefixStrategy != null) {
      prefixStrategy = cacheKeyPrefixStrategy;
    }
  }

  @Override
  public CacheKeyPrefixStrategy getPrefixStrategy() {
    return prefixStrategy;
  }

  @Override
  public Map getStats() {
    Map<String, String> eStatsMap = new HashMap<String, String>(10);

    if (mCache != null) {
      final Statistics cacheStatistics = mCache.getStatistics();
      if (cacheStatistics != null) {
        eStatsMap.put("Associated Cache Name", cacheStatistics.getAssociatedCacheName());
        eStatsMap.put("Average Get Time", String.valueOf(
            cacheStatistics.getAverageGetTime()));
        eStatsMap.put("Cache Hits", String.valueOf(cacheStatistics.getCacheHits()));
        eStatsMap.put("Cache Misses", String.valueOf(cacheStatistics.getCacheMisses()));
        eStatsMap.put("Eviction Count", String.valueOf(cacheStatistics.getEvictionCount()));
        eStatsMap.put("In Memory Hits", String.valueOf(cacheStatistics.getInMemoryHits()));
        eStatsMap.put("On Disk Hits", String.valueOf(cacheStatistics.getOnDiskHits()));
        eStatsMap.put("Object Count", String.valueOf(cacheStatistics.getObjectCount()));
        eStatsMap.put("Statistics Accuracy", String.valueOf(
            cacheStatistics.getStatisticsAccuracy()));
        eStatsMap.put("Statistics Accuracy Description", String.valueOf(
            cacheStatistics.getStatisticsAccuracyDescription()));
      }
    }

    return eStatsMap;
  }

  @Override
  public boolean clearCache() {
    try {
      mCache.removeAll();
      return true;
    }
    catch (Exception ex) {
    }
    return false;
  }

  public Cache getCache() {
    return mCache;
  }

  public void setCache(Cache mCache) {
    this.mCache = mCache;
  }

  private String calculateKey(final Key key) {
    String ePrefix = prefixStrategy.getPrefix();
    String eSeparator = prefixStrategy.getPreferredPrefixIdSeparator();
    StringBuilder eMemCachedKeyId = new StringBuilder();
    if (ePrefix != null) {
      eMemCachedKeyId.append(ePrefix);
      if (eSeparator != null) {
        eMemCachedKeyId.append(eSeparator);
      }
      else {
        eMemCachedKeyId.append(DEFAULT_SEPARATOR);
      }
    }
    eMemCachedKeyId.append(key.toString());
    return eMemCachedKeyId.toString();
  }
}
