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
package com.smartitengineering.common.dao.search.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.smartitengineering.common.dao.search.CommonFreeTextPersistentDao;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class CommonAsyncFreeTextPersistentDaoImpl<T> implements CommonFreeTextPersistentDao<T> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final Queue<T> saveQueue;
  private final Queue<T> updateQueue;
  private final Queue<T> deleteQueue;
  private final long saveScheduleInterval;
  private final long updateScheduleInterval;
  private final long deleteScheduleInterval;
  private final ScheduledExecutorService scheduledExecutorService;
  @Inject
  private CommonFreeTextPersistentDao<T> primaryDao;

  @Inject
  public CommonAsyncFreeTextPersistentDaoImpl(@Named("saveInterval") long saveInterval,
                                              @Named("updateInterval") long updateInterval,
                                              @Named("deleteInterval") long deleteInterval,
                                              @Named("intervalTimeUnit") TimeUnit timeUnit) {
    saveQueue = new ConcurrentLinkedQueue<T>();
    updateQueue = new ConcurrentLinkedQueue<T>();
    deleteQueue = new ConcurrentLinkedQueue<T>();
    scheduledExecutorService = Executors.newScheduledThreadPool(3);
    this.saveScheduleInterval = saveInterval;
    this.updateScheduleInterval = updateInterval;
    this.deleteScheduleInterval = deleteInterval;
    initScheduledThreads(timeUnit);

  }

  private void initScheduledThreads(TimeUnit timeUnit) {
    scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        Iterator<T> iterator = saveQueue.iterator();
        while (iterator.hasNext()) {
          final T next = iterator.next();
          iterator.remove();
          try {
            primaryDao.save(next);
          }
          catch (Exception ex) {
            logger.warn("Saving to search persistent dao did not succeed! Adding back to queue", ex);
            saveQueue.add(next);
          }
        }
      }
    }, saveScheduleInterval, saveScheduleInterval, timeUnit);
    scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        Iterator<T> iterator = updateQueue.iterator();
        while (iterator.hasNext()) {
          final T next = iterator.next();
          iterator.remove();
          try {
            primaryDao.update(next);
          }
          catch (Exception ex) {
            logger.warn("Updating to search persistent dao did not succeed! Adding back to queue", ex);
            updateQueue.add(next);
          }
        }
      }
    }, updateScheduleInterval, updateScheduleInterval, timeUnit);
    scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        Iterator<T> iterator = deleteQueue.iterator();
        while (iterator.hasNext()) {
          final T next = iterator.next();
          iterator.remove();
          try {
            primaryDao.delete(next);
          }
          catch (Exception ex) {
            logger.warn("Deleting to search persistent dao did not succeed! Adding back to queue", ex);
            deleteQueue.add(next);
          }
        }
      }
    }, deleteScheduleInterval, deleteScheduleInterval, timeUnit);
  }

  @Override
  public void save(T... data) {
    saveQueue.addAll(Arrays.asList(data));
  }

  @Override
  public void update(T... data) {
    updateQueue.addAll(Arrays.asList(data));
  }

  @Override
  public void delete(T... data) {
    deleteQueue.addAll(Arrays.asList(data));
  }
}
