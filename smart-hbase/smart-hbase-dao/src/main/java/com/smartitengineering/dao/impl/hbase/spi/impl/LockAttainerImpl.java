/*
 * This is a common dao with basic CRUD operations and is not limited to any
 * persistent layer implementation
 *
 * Copyright (C) 2010  Imran M Yousuf (imyousuf@smartitengineering.com)
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
package com.smartitengineering.dao.impl.hbase.spi.impl;

import com.google.inject.Inject;
import com.smartitengineering.dao.impl.hbase.spi.AsyncExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.Callback;
import com.smartitengineering.dao.impl.hbase.spi.LockAttainer;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class LockAttainerImpl<T, IdType>
    implements LockAttainer<T, IdType> {

  private final Map<T, Map<String, RowLock>> locksCache = new WeakHashMap<T, Map<String, RowLock>>();
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Inject
  private SchemaInfoProvider<T, IdType> infoProvider;
  @Inject
  private AsyncExecutorService executorService;

  @Override
  public synchronized Map<String, RowLock> getLock(final T instance,
                                                   String... tables) {
    logger.info("Attempting to get locks");
    if (locksCache.containsKey(instance)) {
      logger.debug("Got locks from cache!");
      return locksCache.get(instance);
    }
    logger.debug("Not found in cache so trying to retrieve!");
    final Map<String, Future<RowLock>> map = new LinkedHashMap<String, Future<RowLock>>();
    if (tables == null) {
      tables = new String[] {infoProvider.getMainTableName()};
    }
    if (executorService != null) {
      for (String table : tables) {
        Future<RowLock> future = executorService.executeAsynchronously(table, new Callback<RowLock>() {

          @Override
          public RowLock call(HTableInterface tableInterface)
              throws Exception {
            final byte[] rowIdFromRow = infoProvider.getRowIdFromRow(instance);
            if (logger.isDebugEnabled()) {
              logger.debug("Attaining lock for " + Bytes.toString(rowIdFromRow));
            }
            try {
              return tableInterface.lockRow(rowIdFromRow);
            }
            catch (Exception ex) {
              logger.warn(ex.getMessage(), ex);
              throw ex;
            }
          }
        });
        logger.debug("RECEIVED FUTURE Lock");
        map.put(table, future);
      }
    }
    final Map<String, RowLock> lockMap = new HashMap<String, RowLock>(map.size());
    for (Entry<String, Future<RowLock>> lock : map.entrySet()) {
      try {
        lockMap.put(lock.getKey(), lock.getValue().get(infoProvider.getWaitTime(), infoProvider.getUnit()));
      }
      catch (Exception ex) {
        logger.error("Error trying to get lock!", ex);
        throw new RuntimeException(ex);
      }
    }
    locksCache.put(instance, lockMap);
    return lockMap;
  }

  @Override
  public synchronized boolean evictFromCache(T instance) {
    return locksCache.remove(instance) != null;
  }

  @Override
  public synchronized boolean unlockAndEvictFromCache(T instance) {
    Map<String, RowLock> locks = locksCache.get(instance);
    if (locks == null) {
      return false;
    }
    else {
      for (final Entry<String, RowLock> lock : locks.entrySet()) {
        executorService.executeAsynchronously(lock.getKey(), new Callback<Void>() {

          @Override
          public Void call(HTableInterface tableInterface)
              throws Exception {
            tableInterface.unlockRow(lock.getValue());
            return null;
          }
        });
      }
      return false;
    }
  }
}
