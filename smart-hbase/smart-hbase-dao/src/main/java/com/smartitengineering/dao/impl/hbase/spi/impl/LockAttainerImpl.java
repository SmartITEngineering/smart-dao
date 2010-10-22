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
import com.smartitengineering.domain.PersistentDTO;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class LockAttainerImpl<T extends PersistentDTO, IdType>
    implements LockAttainer<T, IdType> {

  private final Map<Key<T>, Map<String, RowLock>> locksCache = new ConcurrentHashMap<Key<T>, Map<String, RowLock>>();
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Inject
  private SchemaInfoProvider<T, IdType> infoProvider;
  @Inject
  private AsyncExecutorService executorService;
  private final ScheduledExecutorService cleanupExecutor;

  {
    cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
    cleanupExecutor.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        Iterator<Entry<Key<T>, Map<String, RowLock>>> entries = locksCache.entrySet().iterator();
        while (entries.hasNext()) {
          Entry<Key<T>, Map<String, RowLock>> entry = entries.next();
          if (entry.getKey().getInstance() == null) {
            entries.remove();
          }
        }
      }
    }, 5, 10, TimeUnit.MINUTES);
  }

  @Override
  public synchronized Map<String, RowLock> getLock(final T instance,
                                                   String... tables) {
    final Key key = new Key(instance);
    if (locksCache.containsKey(key)) {
      return Collections.unmodifiableMap(locksCache.get(key));
    }
    logger.info("Not found in cache so trying to retrieve!");
    final Map<String, Future<RowLock>> map = new LinkedHashMap<String, Future<RowLock>>();
    if (tables == null) {
      tables = new String[]{infoProvider.getMainTableName()};
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
    locksCache.put(key, lockMap);
    return lockMap;
  }

  @Override
  public synchronized boolean evictFromCache(T instance) {
    return locksCache.remove(new Key(instance)) != null;
  }

  @Override
  public void putLock(T instance, Map<String, RowLock> locks) {
    locksCache.put(new Key(instance), locks);
  }

  @Override
  public synchronized boolean unlockAndEvictFromCache(T instance) {
    if (logger.isInfoEnabled()) {
      logger.info("Instance to remove " + " " + instance.getClass() + " " + instance);
      logger.info("Cache " + " " + locksCache.getClass() + " " + locksCache);
    }
    Map<String, RowLock> locks = locksCache.remove(new Key(instance));
    if (locks == null) {
      logger.info("No locks in cache!");
      return false;
    }
    else {
      for (final Entry<String, RowLock> lock : locks.entrySet()) {
        executorService.executeAsynchronously(lock.getKey(), new Callback<Void>() {

          @Override
          public Void call(HTableInterface tableInterface)
              throws Exception {
            final RowLock lockVal = lock.getValue();
            if (logger.isInfoEnabled()) {
              logger.info("Unlocking row: " + lockVal.getLockId());
            }
            tableInterface.unlockRow(lockVal);
            return null;
          }
        });
      }
      return false;
    }
  }

  private static class Key<T extends PersistentDTO> {

    private final WeakReference<T> instance;

    public Key(T instance) {
      this.instance = new WeakReference<T>(instance);
    }

    public T getInstance() {
      return this.instance.get();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Key<T> other = (Key<T>) obj;
      return other.instance.get() == this.instance.get();
    }

    @Override
    public int hashCode() {
      int hash = 5;
      return hash;
    }

    @Override
    public String toString() {
      return "Key{" + "instance=" + instance.get().getId().toString() + '}';
    }
  }
}
