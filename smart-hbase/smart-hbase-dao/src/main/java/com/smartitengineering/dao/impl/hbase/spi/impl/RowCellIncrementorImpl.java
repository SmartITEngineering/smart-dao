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
import com.smartitengineering.dao.impl.hbase.spi.CellConfig;
import com.smartitengineering.dao.impl.hbase.spi.RowCellIncrementor;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class RowCellIncrementorImpl<T, S, Si> implements RowCellIncrementor<T, S, Si> {

  @Inject
  private CellConfig<T> config;
  @Inject
  private SchemaInfoProvider<S, Si> provider;
  @Inject
  private AsyncExecutorService executorService;

  @Override
  public CellConfig<T> getConfig() {
    return config;
  }

  @Override
  public SchemaInfoProvider<S, Si> getProvider() {
    return provider;
  }

  @Override
  public long incrementAndGet(final Si id, final long incrementAmount) {
    return executorService.execute(provider.getMainTableName(), new Callback<Long>() {

      @Override
      public Long call(HTableInterface tableInterface) throws Exception {
        return tableInterface.incrementColumnValue(provider.getRowIdFromId(id), Bytes.toBytes(config.getFamily()), Bytes.
            toBytes(config.getQualifier()), incrementAmount);
      }
    });
  }
}
