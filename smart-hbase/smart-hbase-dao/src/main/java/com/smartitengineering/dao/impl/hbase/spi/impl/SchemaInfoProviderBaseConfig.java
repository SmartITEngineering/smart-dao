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

/**
 *
 * @author imyousuf
 */
public class SchemaInfoProviderBaseConfig<T> {

  private String schemaNamespace, mainTableName, versionColumnFamily, versionColumnQualifier;
  private boolean transactionalDomain;

  public String getVersionColumnFamily() {
    return versionColumnFamily;
  }

  public void setVersionColumnFamily(String versionColumnFamily) {
    this.versionColumnFamily = versionColumnFamily;
  }

  public String getVersionColumnQualifier() {
    return versionColumnQualifier;
  }

  public void setVersionColumnQualifier(String versionColumnQualifier) {
    this.versionColumnQualifier = versionColumnQualifier;
  }

  public String getMainTableName() {
    return mainTableName;
  }

  public void setMainTableName(String mainTableName) {
    this.mainTableName = mainTableName;
  }

  public String getSchemaNamespace() {
    return schemaNamespace;
  }

  public void setSchemaNamespace(String schemaNamespace) {
    this.schemaNamespace = schemaNamespace;
  }

  public boolean isTransactionalDomain() {
    return transactionalDomain;
  }

  public void setTransactionalDomain(boolean transactionalDomain) {
    this.transactionalDomain = transactionalDomain;
  }
}
