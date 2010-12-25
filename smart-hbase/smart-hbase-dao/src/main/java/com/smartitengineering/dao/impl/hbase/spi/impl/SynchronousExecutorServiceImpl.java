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

import com.smartitengineering.dao.impl.hbase.HBaseConfigurationFactory;
import com.smartitengineering.dao.impl.hbase.spi.Callback;
import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class SynchronousExecutorServiceImpl implements ExecutorService {

  public static final int DEFAULT_MAX_HTABLE_POOL_SIZE = 3000;
  private Configuration configuration;
  private HTablePool tablePool;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected Configuration getConfiguration() {
    if (configuration == null) {
      logger.info("Trying to get configuration from configuration factory...");
      configuration = HBaseConfigurationFactory.getConfigurationInstance();
      if (configuration == null) {
        logger.info("Initializing configuration");
        configuration = HBaseConfiguration.create();
      }
    }
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  protected HTablePool getTablePool() {
    if (tablePool == null) {
      logger.info("Initializing table pool!");
      tablePool = new HTablePool(getConfiguration(), DEFAULT_MAX_HTABLE_POOL_SIZE);
    }
    return tablePool;
  }

  @Override
  public <ReturnType> ReturnType execute(String tableName,
                                         Callback<ReturnType> callback) {
    final HTableInterface tableInterface;
    if (StringUtils.isBlank(tableName)) {
      throw new IllegalArgumentException("Table name not provided!");
    }
    else {
      tableInterface = getTablePool().getTable(tableName);
    }
    try {
      return callback.call(tableInterface);
    }
    catch (Exception ex) {
      logger.warn("Exception in callback!", ex);
      throw new RuntimeException(ex);
    }
    finally {
      try {
        if (tableInterface != null) {
          getTablePool().putTable(tableInterface);
        }
      }
      catch (Exception ex) {
        logger.warn("Could not put table!", ex);
      }
    }
  }
}
