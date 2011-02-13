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
import java.util.ArrayList;
import java.util.List;
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
  private final Queue<Task<T[]>> saveQueue;
  private final long saveScheduleInterval;
  private final ScheduledExecutorService scheduledExecutorService;
  @Inject
  @Named("primaryFreeTextPersistentDao")
  private CommonFreeTextPersistentDao<T> primaryDao;

  @Inject
  public CommonAsyncFreeTextPersistentDaoImpl(@Named("saveInterval") long saveInterval,
                                              @Named("intervalTimeUnit") TimeUnit timeUnit) {
    saveQueue = new ConcurrentLinkedQueue<Task<T[]>>();
    scheduledExecutorService = Executors.newScheduledThreadPool(1);
    this.saveScheduleInterval = saveInterval;
    initScheduledThreads(timeUnit);

  }

  private void initScheduledThreads(TimeUnit timeUnit) {
    scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        List<Task<T[]>> nextTasks = new ArrayList<Task<T[]>>();
        Task<T[]> nextTask;
        do {
          nextTask = saveQueue.poll();
          if (nextTask != null && nextTask.getEntity() != null) {
            nextTasks.add(nextTask);
          }
        }
        while (nextTask != null);
        for (Task<T[]> task : nextTasks) {
          T[] next = task.getEntity();
          try {
            switch (task.getTaskType()) {
              case CREATE:
                primaryDao.save(next);
                break;
              case UPDATE:
                primaryDao.update(next);
                break;
              case DELETE:
                primaryDao.delete(next);
                break;
            }
          }
          catch (Exception ex) {
            logger.warn("Persisting to search persistent dao did not succeed! Adding back to queue", ex);
            saveQueue.add(task);
          }
        }
      }
    }, saveScheduleInterval, saveScheduleInterval, timeUnit);
  }

  @Override
  public void save(T... data) {
    addToTaskQueue(TaskType.CREATE, data);
  }

  @Override
  public void update(T... data) {
    addToTaskQueue(TaskType.UPDATE, data);
  }

  @Override
  public void delete(T... data) {
    addToTaskQueue(TaskType.DELETE, data);
  }

  private void addToTaskQueue(TaskType type, T... entities) {
    saveQueue.add(new Task<T[]>(type, entities));
  }

  private static enum TaskType {

    CREATE, UPDATE, DELETE;
  }

  private static class Task<P> {

    private TaskType taskType;
    private P entity;

    public Task(TaskType taskType, P entity) {
      this.taskType = taskType;
      this.entity = entity;
    }

    public P getEntity() {
      return entity;
    }

    public TaskType getTaskType() {
      return taskType;
    }
  }
}
