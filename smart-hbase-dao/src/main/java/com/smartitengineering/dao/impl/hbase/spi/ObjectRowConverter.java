/*
 * This is a common dao with basic CRUD operations and is not limited to any
 * persistent layer implementation
 *
 * Copyright (C) 2008  Imran M Yousuf (imyousuf@smartitengineering.com)
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

import java.util.LinkedHashMap;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

/**
 * Convert an object from object to HBase row and vice-versa
 * @author imyousuf
 */
public interface ObjectRowConverter<T> {

  /**
   * Convert an object to its representive class-row tuple. The Class will later
   * be used to determine which table to put the row into as well.
   * @param instance The object instance to convert into updateable rows
   * @return An ordered {@link java.util.Map} with related {@link Put}.
   */
  LinkedHashMap<Class, Put> objectToRows(T instance);

  /**
   * Load an instance of the convertable object with the row. It might be needed
   * to load more {@link Result} to be able to load the object.
   * @param startRow The root row of this object.
   * @return Instance of the object from persisted {@link Result result(s)}
   */
  T rowsToObject(Result startRow);

}
