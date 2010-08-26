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

import com.smartitengineering.dao.impl.hbase.spi.AsyncExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.Callback;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author imyousuf
 */
public class AsynchronousExecutorServiceImpl extends SynchronousExecutorServiceImpl implements AsyncExecutorService {

  private ExecutorService executorService;
  private int maxThreads = -1;
  private long timeoutPeriod = 5;
  private TimeUnit timeUnit = TimeUnit.SECONDS;

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

  public void setTimeUnit(String timeUnitStr) {
    this.timeUnit = TimeUnit.valueOf(timeUnitStr);
  }

  public void setTimeUnit(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
  }

  public long getTimeoutPeriod() {
    return timeoutPeriod;
  }

  public void setTimeoutPeriod(long timeoutPeriod) {
    this.timeoutPeriod = timeoutPeriod;
  }

  public int getMaxThreads() {
    return maxThreads;
  }

  public void setMaxThreads(int maxThreads) {
    this.maxThreads = maxThreads;
  }

  public ExecutorService getExecutorService() {
    if (executorService == null) {
      if (maxThreads <= 0) {
        executorService = Executors.newCachedThreadPool();
      }
      else if (maxThreads == 1) {
        executorService = Executors.newSingleThreadExecutor();
      }
      else {
        executorService = Executors.newFixedThreadPool(maxThreads);
      }
    }
    return executorService;
  }

  @Override
  public <ReturnType> ReturnType execute(final String tableName,
                                         final Callback<ReturnType> callback) {
    try {
      return executeAsynchronously(tableName, callback).get(getTimeoutPeriod(), getTimeUnit());
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public <ReturnType> Future<ReturnType> executeAsynchronously(final String tableName,
                                                               final Callback<ReturnType> callback) {
    return getExecutorService().submit(new Callable<ReturnType>() {

      @Override
      public ReturnType call() throws Exception {
        return plainSyncExecution(tableName, callback);
      }
    });
  }

  protected <ReturnType> ReturnType plainSyncExecution(final String tableName, final Callback<ReturnType> callback) {
    return AsynchronousExecutorServiceImpl.super.execute(tableName, callback);
  }
}
