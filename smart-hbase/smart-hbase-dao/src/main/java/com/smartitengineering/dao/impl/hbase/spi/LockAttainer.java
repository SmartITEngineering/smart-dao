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
package com.smartitengineering.dao.impl.hbase.spi;

import java.util.Map;
import org.apache.hadoop.hbase.client.RowLock;

/**
 *
 * @author imyousuf
 */
public interface LockAttainer<T, IdType> {

  Map<String, RowLock> getLock(T instance,
                               String... tables);

  void putLock(T instance, Map<String, RowLock> locks);

  boolean evictFromCache(T instance);

  boolean unlockAndEvictFromCache(T instance);
}
