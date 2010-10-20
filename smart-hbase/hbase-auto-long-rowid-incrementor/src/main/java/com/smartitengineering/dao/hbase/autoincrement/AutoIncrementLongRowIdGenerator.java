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
package com.smartitengineering.dao.hbase.autoincrement;

import com.sun.jersey.spi.resource.Singleton;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
@Path("/{tableName}")
@Singleton
public class AutoIncrementLongRowIdGenerator {

  private Configuration hbaseConfiguration;
  private final ConcurrentHashMap<String, AtomicLong> tableCurrentMax = new ConcurrentHashMap<String, AtomicLong>();
  private HTablePool pool;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public Configuration getHbaseConfiguration() {
    if (hbaseConfiguration == null) {
      synchronized (this) {
        if (hbaseConfiguration == null) {
          hbaseConfiguration = HBaseConfiguration.create();
        }
      }
    }
    return hbaseConfiguration;
  }

  public HTablePool getPool() {
    if (pool == null) {
      synchronized (this) {
        if (pool == null) {
          pool = new HTablePool(getHbaseConfiguration(), 200);
        }
      }
    }
    return pool;
  }

  @POST
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response get(@PathParam("tableName") String tableName) throws IOException {
    final HTableInterface table = getPool().getTable(tableName);
    try {
      AtomicLong mutableLong = tableCurrentMax.get(tableName);
      if (mutableLong == null) {
        AtomicLong along = new AtomicLong(-1);
        mutableLong = tableCurrentMax.putIfAbsent(tableName, along);
        if (mutableLong == null) {
          mutableLong = along;
        }
        synchronized (mutableLong) {
          if (mutableLong.longValue() < 0) {
            Scan scan = new Scan();
            scan.setCaching(1);
            ResultScanner scanner = table.getScanner(scan);
            try {
              Result result[];
              result = scanner.next(1);
              if (result != null && result.length > 0) {
                mutableLong.set(Bytes.toLong(result[0].getRow()));
              }
              else {
                mutableLong.set(Long.MAX_VALUE);
              }
            }
            finally {
              if (scanner != null) {
                try {
                  scanner.close();
                }
                catch (Exception ex) {
                  logger.warn("Could not close scanner!", ex);
                }
              }
            }
          }
        }
      }
      long newId = mutableLong.decrementAndGet();
      boolean rowExists = table.exists(new Get(Bytes.toBytes(newId)));
      while (rowExists) {
        newId = mutableLong.decrementAndGet();
        rowExists = table.exists(new Get(Bytes.toBytes(newId)));
      }
      final long id = newId;
      return Response.ok(Bytes.toBytes(id)).build();
    }
    finally {
      if (table != null) {
        getPool().putTable(table);
      }
    }
  }
}
