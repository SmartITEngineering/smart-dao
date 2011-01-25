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
import com.smartitengineering.dao.impl.hbase.spi.LockType;
import com.smartitengineering.dao.impl.hbase.spi.MergeService;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class DiffBasedMergeService<T, IdType> implements MergeService<T, IdType> {

  @Inject
  private SchemaInfoProvider<T, IdType> infoProvider;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void merge(final HTableInterface tableInterface, final List<Put> puts, LockType lockType) throws IOException {
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
            if (logger.isInfoEnabled()) {
              logger.info("Deleting cell from family - " + Bytes.toString(family) + " and assuming string key " + Bytes.
                  toString(entry.getKey()));
            }
            delete.deleteColumn(family, entry.getKey());
          }
        }
      }
      byte[] family = infoProvider.getVersionColumnFamily();
      byte[] qualifier = infoProvider.getVersionColumnQualifier();
      if (hasDiff) {
        if (lockType.equals(LockType.OPTIMISTIC) && family != null && qualifier != null) {
          final List<KeyValue> vals = put.get(family, qualifier);
          KeyValue value = getLatestValue(vals);
          tableInterface.checkAndDelete(row, family, qualifier, value.getValue(), delete);
        }
        else {
          deletes.add(delete);
        }
      }
    }
    if (!deletes.isEmpty()) {
      tableInterface.delete(deletes);
    }
  }

  public static KeyValue getLatestValue(List<KeyValue> vals) {
    return Collections.max(vals, new Comparator<KeyValue>() {

      @Override
      public int compare(KeyValue o1, KeyValue o2) {
        return new Long(o1.getTimestamp()).compareTo(o2.getTimestamp());
      }
    });
  }
}
