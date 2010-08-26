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

/**
 * An API for executing HBase requests.
 * @author imyousuf
 */
public interface ExecutorService {

  /**
   * Execute a HBase request and use the callback to perform the custom part and return if needed.
   * @param <ReturnType> The return type of the execution, may be {@link Void}
   * @param tableName Name of the table the callback will perform action on. May be blank, check implementation for
   *                  details
   * @param callback The custom action callback
   * @return Return whatever is wished from the callback to the executor.
   */
  public <ReturnType> ReturnType execute(String tableName, Callback<ReturnType> callback);
}
