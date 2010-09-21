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
import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.ObjectRowConverter;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RowLock;

/**
 *
 * @author imyousuf
 */
public abstract class AbstactObjectRowConverter<T, IdType> implements ObjectRowConverter<T> {

  @Inject
  private SchemaInfoProvider<T, IdType> infoProvider;

  @Override
  public LinkedHashMap<String, Put> objectToRows(final T instance, final ExecutorService service) {
    final AsyncExecutorService executorService;
    if (service instanceof AsyncExecutorService) {
      executorService = (AsyncExecutorService) service;
    }
    else {
      executorService = null;
    }
    LinkedHashMap<String, Put> puts = new LinkedHashMap<String, Put>();
    String[] tables = getTablesToAttainLock();
    Map<String, Future<RowLock>> map = getLocks(executorService, instance, tables);
    if (tables != null) {
      for (String table : tables) {
        try {
          RowLock lock = map.get(table).get(infoProvider.getWaitTime(), infoProvider.getUnit());
          final Put put;
          if(lock == null) {
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

  protected Map<String, Future<RowLock>> getLocks(AsyncExecutorService executorService, final T instance,
                                                  String... tables) {
    Map<String, Future<RowLock>> map = new LinkedHashMap<String, Future<RowLock>>();
    if (tables != null) {
      if (executorService != null) {
        for (String table : tables) {
          Future<RowLock> future = executorService.executeAsynchronously(table, new Callback<RowLock>() {

            @Override
            public RowLock call(HTableInterface tableInterface) throws Exception {
              return tableInterface.lockRow(infoProvider.getRowIdFromRow(instance));
            }
          });
          map.put(table, future);
        }
      }
    }
    return map;
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
    Map<String, Future<RowLock>> map = getLocks(executorService, instance, tables);
    if (tables != null) {
      for (String table : tables) {
        try {
          RowLock lock = map.get(table).get(infoProvider.getWaitTime(), infoProvider.getUnit());
          final Delete delete;
          if(lock == null) {
            delete = new Delete(infoProvider.getRowIdFromRow(instance));
          }
          else {
            delete = new Delete(infoProvider.getRowIdFromRow(instance), System.currentTimeMillis(), lock);
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

  @Override
  public LinkedHashMap<String, Put> objectToRows(T instance) {
    return objectToRows(instance, null);
  }

  @Override
  public LinkedHashMap<String, Delete> objectToDeleteableRows(T instance) {
    return objectToDeleteableRows(instance, null);
  }

  protected SchemaInfoProvider<T, IdType> getInfoProvider() {
    return infoProvider;
  }

  protected abstract String[] getTablesToAttainLock();

  protected abstract void getPutForTable(T instance, ExecutorService service, Put put);

  protected abstract void getDeleteForTable(T instance, ExecutorService service, Delete put);
}
