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

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A class to provide information related to schema.
 * @author imyousuf
 */
public interface SchemaInfoProvider<T, IdType> {

  /**
   * Get the namespace for distinguishing this schema info provider.
   * @return Namespace for this schema info provider
   */
  String getSchemaNamespace();

  /**
   * A Generic configuration on whether the writes are to be performed in async
   * manner or not.
   * @return True if transactional else false
   */
  boolean isTransactionalDomain();

  /**
   * Given an object provide the the classes it converts with their respective
   * table names.
   * N.B. This method will not be used just yet, it will be used later if and when an Object-BigTable Mapping is
   * implemented.
   * @param domainInstance Domain object to check for.
   * @return Domain representations mapped by HBase table names
   */
  Map<String, Class> getTableNames(Object domainInstance);

  /**
   * Retrieve the main table name to use for {@link Get Get's}.
   * @return The main table name
   */
  String getMainTableName();

  /**
   * Filter config for the specified property name will be retrieved.
   * @param propertyName The property to retrieve the config for
   * @return Configuration of the property.
   */
  FilterConfig getFilterConfig(String propertyName);

  byte[] getVersionColumnFamily();

  byte[] getVersionColumnQualifier();

  byte[] getRowIdFromRow(T instance) throws IOException;

  byte[] getRowIdFromId(IdType id) throws IOException;

  IdType getIdFromRowId(byte[] id) throws IOException, ClassNotFoundException;

  long getWaitTime();

  TimeUnit getUnit();
}
