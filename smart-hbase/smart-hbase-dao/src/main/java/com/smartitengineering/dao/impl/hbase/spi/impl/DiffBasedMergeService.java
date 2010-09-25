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

import com.smartitengineering.dao.impl.hbase.spi.MergeService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

/**
 *
 * @author imyousuf
 */
public class DiffBasedMergeService implements MergeService {

  @Override
  public void merge(final HTableInterface tableInterface, final List<Put> puts) throws IOException {
    List<Delete> deletes = new ArrayList<Delete>(puts.size());
    final long timestampForDelete = System.currentTimeMillis();
    for (Put put : puts) {
      final byte[] row = put.getRow();
      Get get = new Get(row, put.getRowLock());
      Result result = tableInterface.get(get);
      if (result.isEmpty()) {
        continue;
      }
      boolean hasDiff = false;
      Delete delete = new Delete(row, timestampForDelete, put.getRowLock());
      for (byte[] family : put.getFamilyMap().keySet()) {
        for (Entry<byte[], byte[]> entry : result.getFamilyMap(family).entrySet()) {
          if (!put.has(family, entry.getKey())) {
            hasDiff = true;
            delete.deleteColumn(family, entry.getKey());
          }
        }
      }
      if (hasDiff) {
        deletes.add(delete);
      }
    }
    tableInterface.delete(deletes);
  }
}
