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
package com.smartitengineering.dao.hbase.ddl.config;

import com.smartitengineering.dao.hbase.ddl.HBaseColumnFamilyConfiguration;
import com.smartitengineering.dao.hbase.ddl.HBaseTableConfiguration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 *
 * @author imyousuf
 */
@JsonDeserialize(as=HBaseTableConfigurationImpl.class)
public class HBaseTableConfigurationImpl implements HBaseTableConfiguration {

  private String tableName;
  private final Collection<HBaseColumnFamilyConfiguration> columnFamilyConfigurations;

  {
    columnFamilyConfigurations = new ArrayList<HBaseColumnFamilyConfiguration>();
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  @Override
  public Collection<HBaseColumnFamilyConfiguration> getColumnFamilyConfigurations() {
    return Collections.unmodifiableCollection(columnFamilyConfigurations);
  }

  public void setColumnFamilyConfigurations(Collection<HBaseColumnFamilyConfiguration> columnFamilyConfigurations) {
    this.columnFamilyConfigurations.clear();
    if (columnFamilyConfigurations != null && !columnFamilyConfigurations.isEmpty()) {
      this.columnFamilyConfigurations.addAll(columnFamilyConfigurations);
    }
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}
