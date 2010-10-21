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
import com.smartitengineering.domain.PersistentDTO;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public abstract class AbstractObjectRowConverter<T extends PersistentDTO<? extends PersistentDTO, ? extends Comparable, Long>, IdType>
    implements ObjectRowConverter<T> {

  @Inject
  private SchemaInfoProvider<T, IdType> infoProvider;
  @Inject
  private LockAttainer<T, IdType> lockAttainer;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public LinkedHashMap<String, Put> objectToRows(final T instance, final ExecutorService service,
                                                 boolean pessimisticLock) {
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
    Map<String, RowLock> map = getLocks(executorService, instance, pessimisticLock, tables);
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
          final byte[] family = infoProvider.getVersionColumnFamily();
          final byte[] qualifier = infoProvider.getVersionColumnQualifier();
          if (family != null && qualifier != null) {
            put.add(family, qualifier, Bytes.toBytes(instance.getVersion()));
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

  protected Map<String, RowLock> getLocks(AsyncExecutorService executorService, final T instance,
                                          boolean pessimisticLock, String... tables) {
    logger.info("Attempting to get locks");
    if (pessimisticLock) {
      return lockAttainer.getLock(instance, tables);
    }
    else {
      return Collections.emptyMap();
    }
  }

  @Override
  public LinkedHashMap<String, Delete> objectToDeleteableRows(T instance, ExecutorService service,
                                                              boolean pessimisticLock) {
    AsyncExecutorService executorService;
    if (service instanceof AsyncExecutorService) {
      executorService = (AsyncExecutorService) service;
    }
    else {
      executorService = null;
    }
    LinkedHashMap<String, Delete> deletes = new LinkedHashMap<String, Delete>();
    String[] tables = getTablesToAttainLock();
    Map<String, RowLock> map = getLocks(executorService, instance, pessimisticLock, tables);
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

  protected void addColumn(String family, String column, String value, Put put) {
    addColumn(Bytes.toBytes(family), Bytes.toBytes(column), value, put);
  }

  protected void addColumn(byte[] family, byte[] column, String value, Put put) {
    addColumn(family, column, Bytes.toBytes(value), put);
  }

  protected void addColumn(String family, String column, Long value, Put put) {
    addColumn(Bytes.toBytes(family), Bytes.toBytes(column), value, put);
  }

  protected void addColumn(byte[] family, byte[] column, Long value, Put put) {
    addColumn(family, column, Bytes.toBytes(value), put);
  }

  protected void addColumn(String family, String column, Integer value, Put put) {
    addColumn(Bytes.toBytes(family), Bytes.toBytes(column), value, put);
  }

  protected void addColumn(byte[] family, byte[] column, Integer value, Put put) {
    addColumn(family, column, Bytes.toBytes(value), put);
  }

  protected void addColumn(String family, String column, Double value, Put put) {
    addColumn(Bytes.toBytes(family), Bytes.toBytes(column), value, put);
  }

  protected void addColumn(byte[] family, byte[] column, Double value, Put put) {
    addColumn(family, column, Bytes.toBytes(value), put);
  }

  protected void addColumn(String family, String column, Boolean value, Put put) {
    addColumn(Bytes.toBytes(family), Bytes.toBytes(column), value, put);
  }

  protected void addColumn(byte[] family, byte[] column, Boolean value, Put put) {
    addColumn(family, column, Bytes.toBytes(value), put);
  }

  protected void addColumn(byte[] family, byte[] column, byte[] value, Put put) {
    put.add(family, column, value);
  }

  protected void populateVersion(T instance, Result row) {
    byte[] verFam = getInfoProvider().getVersionColumnFamily();
    byte[] verQual = getInfoProvider().getVersionColumnQualifier();
    if (verFam != null && verQual != null && row.containsColumn(verFam, verQual)) {
      instance.setVersion(Long.valueOf(Bytes.toLong(row.getValue(verFam, verQual))));
    }
  }

  protected abstract String[] getTablesToAttainLock();

  protected abstract void getPutForTable(T instance, ExecutorService service, Put put);

  protected abstract void getDeleteForTable(T instance, ExecutorService service, Delete put);
}
