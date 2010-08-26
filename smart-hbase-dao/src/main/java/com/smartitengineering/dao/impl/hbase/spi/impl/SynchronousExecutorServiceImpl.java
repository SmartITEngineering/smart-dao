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

import com.smartitengineering.dao.impl.hbase.spi.Callback;
import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

/**
 *
 * @author imyousuf
 */
public class SynchronousExecutorServiceImpl implements ExecutorService {

  public static final int DEFAULT_MAX_HTABLE_POOL_SIZE = 3000;
  private Configuration configuration;
  private HTablePool tablePool;
  private SchemaInfoProvider infoProvider;

  public SchemaInfoProvider getInfoProvider() {
    return infoProvider;
  }

  public void setInfoProvider(SchemaInfoProvider infoProvider) {
    this.infoProvider = infoProvider;
  }

  protected Configuration getConfiguration() {
    if (configuration == null) {
      configuration = HBaseConfiguration.create();
    }
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  protected HTablePool getTablePool() {
    if (tablePool == null) {
      tablePool = new HTablePool(getConfiguration(), DEFAULT_MAX_HTABLE_POOL_SIZE);
    }
    return tablePool;
  }

  @Override
  public <ReturnType> ReturnType execute(String tableName,
                                         Callback<ReturnType> callback) {
    final HTableInterface tableInterface;
    if (StringUtils.isBlank(tableName)) {
      tableInterface = getDefaultTable();
    }
    else {
      tableInterface = getTablePool().getTable(tableName);
    }
    try {
      return callback.call(tableInterface);
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    finally {
      try {
        if (tableInterface != null) {
          getTablePool().putTable(tableInterface);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  protected HTableInterface getDefaultTable() {
    return getTablePool().getTable(getInfoProvider().getMainTableName());
  }
}
