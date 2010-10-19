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
import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.LockAttainer;
import com.smartitengineering.dao.impl.hbase.spi.ObjectRowConverter;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RowLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public abstract class AbstactObjectRowConverter<T, IdType> implements ObjectRowConverter<T> {

  @Inject
  private SchemaInfoProvider<T, IdType> infoProvider;
  @Inject
  private LockAttainer<T, IdType> lockAttainer;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public LinkedHashMap<String, Put> objectToRows(final T instance, final ExecutorService service) {
    final AsyncExecutorService executorService;
    if (service instanceof AsyncExecutorService) {
      executorService = (AsyncExecutorService) service;
    }
    else {
      executorService = null;
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Executor service " + executorService + " " + executorService);
    }
    LinkedHashMap<String, Put> puts = new LinkedHashMap<String, Put>();
    String[] tables = getTablesToAttainLock();
    Map<String, RowLock> map = getLocks(executorService, instance, tables);
    if (tables != null) {
      for (String table : tables) {
        try {
          if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Working with table ").append(table).toString());
            logger.debug(new StringBuilder("Future ").append(map.get(table)).toString());
          }
          RowLock lock = map.get(table);
          final Put put;
          if (lock == null) {
            put = new Put(infoProvider.getRowIdFromRow(instance));
          }
          else {
            put = new Put(infoProvider.getRowIdFromRow(instance), lock);
          }
          getPutForTable(instance, service, put);
          puts.put(table, put);
        }
        catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    }
    return puts;
  }

  protected Map<String, RowLock> getLocks(AsyncExecutorService executorService, final T instance, String... tables) {
    logger.info("Attempting to get locks");
    return lockAttainer.getLock(instance, tables);
  }

  @Override
  public LinkedHashMap<String, Delete> objectToDeleteableRows(T instance, ExecutorService service) {
    AsyncExecutorService executorService;
    if (service instanceof AsyncExecutorService) {
      executorService = (AsyncExecutorService) service;
    }
    else {
      executorService = null;
    }
    LinkedHashMap<String, Delete> deletes = new LinkedHashMap<String, Delete>();
    String[] tables = getTablesToAttainLock();
    Map<String, RowLock> map = getLocks(executorService, instance, tables);
    if (tables != null) {
      for (String table : tables) {
        try {
          RowLock lock = map.get(table);
          final Delete delete;
          if (lock == null) {
            delete = new Delete(infoProvider.getRowIdFromRow(instance));
          }
          else {
            delete = new Delete(infoProvider.getRowIdFromRow(instance), HConstants.LATEST_TIMESTAMP, lock);
          }
          getDeleteForTable(instance, service, delete);
          deletes.put(table, delete);
        }
        catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    }
    return deletes;
  }

  protected SchemaInfoProvider<T, IdType> getInfoProvider() {
    return infoProvider;
  }

  protected abstract String[] getTablesToAttainLock();

  protected abstract void getPutForTable(T instance, ExecutorService service, Put put);

  protected abstract void getDeleteForTable(T instance, ExecutorService service, Delete put);
}
