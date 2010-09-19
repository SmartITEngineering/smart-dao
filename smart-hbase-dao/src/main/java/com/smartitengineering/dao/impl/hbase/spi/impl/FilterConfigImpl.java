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
package com.smartitengineering.dao.impl.hbase.spi.impl;

import com.smartitengineering.dao.impl.hbase.spi.FilterConfig;
import java.util.Arrays;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author imyousuf
 */
public class FilterConfigImpl implements FilterConfig {

  private byte[] columnFamily, columnQualifier;
  private boolean filterOnIfMissing, filterOnLatestVersionOnly, qualifierARangePrefix;
  private boolean filterOnRowId;

  public void setFilterOnRowId(boolean filterOnRowId) {
    this.filterOnRowId = filterOnRowId;
  }

  public void setQualifierARangePrefix(boolean qualifierARangePrefix) {
    this.qualifierARangePrefix = qualifierARangePrefix;
  }

  @JsonProperty
  public void setColumnFamilyAsString(String columnFamily) {
    this.setColumnFamily(Bytes.toBytes(columnFamily));
  }

  @JsonProperty
  public void setColumnQualifierAsString(String columnQualifier) {
    this.setColumnQualifier(Bytes.toBytes(columnQualifier));
  }

  @JsonIgnore
  public void setColumnFamily(byte[] columnFamily) {
    this.columnFamily = columnFamily;
  }

  @JsonIgnore
  public void setColumnQualifier(byte[] columnQualifier) {
    this.columnQualifier = columnQualifier;
  }

  public void setFilterOnIfMissing(boolean filterOnIfMissing) {
    this.filterOnIfMissing = filterOnIfMissing;
  }

  public void setFilterOnLatestVersionOnly(boolean filterOnLatestVersionOnly) {
    this.filterOnLatestVersionOnly = filterOnLatestVersionOnly;
  }

  public String getColumnFamilyAsString() {
    return Bytes.toString(this.columnFamily);
  }

  public String getColumnQualifierAsString() {
    return Bytes.toString(this.columnQualifier);
  }

  @Override
  @JsonIgnore
  public byte[] getColumnFamily() {
    return this.columnFamily;
  }

  @Override
  @JsonIgnore
  public byte[] getColumnQualifier() {
    return this.columnQualifier;
  }

  @Override
  public boolean isFilterOnIfMissing() {
    return this.filterOnIfMissing;
  }

  @Override
  public boolean isFilterOnLatestVersionOnly() {
    return this.filterOnLatestVersionOnly;
  }

  @Override
  public boolean isQualifierARangePrefix() {
    return qualifierARangePrefix;
  }

  @Override
  public boolean isFilterOnRowId() {
    return filterOnRowId;
  }

  @Override
  public String toString() {
    return "FilterConfigImpl{" + "columnFamily=" + Bytes.toString(columnFamily) + ", columnQualifier=" +
        Bytes.toString(columnQualifier) + ", filterOnIfMissing=" + filterOnIfMissing + ", filterOnLatestVersionOnly=" +
        filterOnLatestVersionOnly + ", qualifierARangePrefix=" + qualifierARangePrefix + ", filterOnRowId=" +
        filterOnRowId + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!FilterConfig.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final FilterConfig other = (FilterConfig) obj;
    if (!Arrays.equals(this.columnFamily, other.getColumnFamily())) {
      return false;
    }
    if (!Arrays.equals(this.columnQualifier, other.getColumnQualifier())) {
      return false;
    }
    if (this.filterOnIfMissing != other.isFilterOnIfMissing()) {
      return false;
    }
    if (this.filterOnLatestVersionOnly != other.isFilterOnLatestVersionOnly()) {
      return false;
    }
    if (this.qualifierARangePrefix != other.isQualifierARangePrefix()) {
      return false;
    }
    if (this.filterOnRowId != other.isFilterOnRowId()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 17 * hash + Arrays.hashCode(this.columnFamily);
    hash = 17 * hash + Arrays.hashCode(this.columnQualifier);
    hash = 17 * hash + (this.filterOnIfMissing ? 1 : 0);
    hash = 17 * hash + (this.filterOnLatestVersionOnly ? 1 : 0);
    hash = 17 * hash + (this.qualifierARangePrefix ? 1 : 0);
    hash = 17 * hash + (this.filterOnRowId ? 1 : 0);
    return hash;
  }
}
