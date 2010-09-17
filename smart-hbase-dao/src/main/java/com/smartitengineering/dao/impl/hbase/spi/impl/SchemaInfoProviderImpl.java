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
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author imyousuf
 */
public class SchemaInfoProviderImpl<T> implements SchemaInfoProvider<T> {

  private String schemaNamespace, mainTableName;
  private boolean transactionalDomain;
  private Map<String, FilterConfig> filterConfigs;

  public SchemaInfoProviderImpl() {
    filterConfigs = new HashMap<String, FilterConfig>();
  }

  public Map<String, FilterConfig> getFilterConfigs() {
    return Collections.unmodifiableMap(filterConfigs);
  }

  public void addFilterConfig(String propertyName, FilterConfig filterConfig) {
    this.filterConfigs.put(propertyName, filterConfig);
  }

  public FilterConfig removeFilterConfig(String propertyName) {
    return this.filterConfigs.remove(propertyName);
  }

  public void setFilterConfigs(Map<String, FilterConfig> filterConfigs) {
    this.filterConfigs.clear();
    if (filterConfigs != null && !filterConfigs.isEmpty()) {
      this.filterConfigs.putAll(filterConfigs);
    }
  }

  public void setMainTableName(String mainTableName) {
    this.mainTableName = mainTableName;
  }

  public void setSchemaNamespace(String schemaNamespace) {
    this.schemaNamespace = schemaNamespace;
  }

  public void setTransactionalDomain(boolean transactionalDomain) {
    this.transactionalDomain = transactionalDomain;
  }

  @Override
  public String getSchemaNamespace() {
    return schemaNamespace;
  }

  @Override
  public boolean isTransactionalDomain() {
    return transactionalDomain;
  }

  @Override
  public Map<String, Class> getTableNames(Object domainInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getMainTableName() {
    return mainTableName;
  }

  @Override
  public FilterConfig getFilterConfig(String propertyName) {
    return filterConfigs.get(propertyName);
  }
}
