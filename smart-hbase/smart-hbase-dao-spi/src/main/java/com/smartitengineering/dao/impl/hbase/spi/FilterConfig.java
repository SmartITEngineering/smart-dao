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

/**
 * Configurations encompassing a filter of a specific column. Particularly designed to be used with the filter
 * {@link SingleColumnValueFilter}
 * @author imyousuf
 */
public interface FilterConfig {

  /**
   * Retrieve the column family byte array.
   * @return Column family
   */
  byte[] getColumnFamily();

  /**
   * Retrieve the column qualifier.
   * @return Column qualifier
   */
  byte[] getColumnQualifier();

  /**
   * Boolean flag denoting whether to ignore rows where the cell is missing
   * @return True to filter, false to ignore
   */
  boolean isFilterOnIfMissing();

  /**
   * Boolean flag to denote whether search will take place on latest version only or not.
   * @return True if only on latest version, false if on all version.
   */
  boolean isFilterOnLatestVersionOnly();

  /**
   * Denotes whether the qualifier signifies a Cell or a range of cells.
   * @return True if range of cells, false if a single column
   */
  boolean isQualifierARangePrefix();

  /**
   * We need a way to determine to add row filter and this config will give us just that.
   * @return
   */
  boolean isFilterOnRowId();
}
