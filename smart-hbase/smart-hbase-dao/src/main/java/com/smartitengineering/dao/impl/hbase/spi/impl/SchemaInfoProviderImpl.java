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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.smartitengineering.dao.impl.hbase.spi.DomainIdInstanceProvider;
import com.smartitengineering.dao.impl.hbase.spi.Externalizable;
import com.smartitengineering.dao.impl.hbase.spi.FilterConfig;
import com.smartitengineering.dao.impl.hbase.spi.FilterConfigs;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import com.smartitengineering.domain.PersistentDTO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class SchemaInfoProviderImpl<T extends PersistentDTO, IdType> implements SchemaInfoProvider<T, IdType> {

  private String schemaNamespace, mainTableName;
  private byte[] versionColumnFamily, versionColumnQualifier;
  private boolean transactionalDomain;
  private Map<String, FilterConfig> filterConfigs;
  private long waitTime;
  private TimeUnit unit;
  @Inject
  Class<IdType> idTypeClass;
  @Inject
  DomainIdInstanceProvider provider;

  public SchemaInfoProviderImpl() {
    filterConfigs = new HashMap<String, FilterConfig>();
  }

  public Class<IdType> getIdTypeClass() {
    return idTypeClass;
  }

  public void setIdTypeClass(Class<IdType> idTypeClass) {
    this.idTypeClass = idTypeClass;
  }

  @Inject
  public void setBaseConfig(SchemaInfoProviderBaseConfig<T> config) {
    this.schemaNamespace = config.getSchemaNamespace();
    this.mainTableName = config.getMainTableName();
    this.transactionalDomain = config.isTransactionalDomain();
    if (StringUtils.isNotBlank(config.getVersionColumnFamily())) {
      this.versionColumnFamily = Bytes.toBytes(config.getVersionColumnFamily());
    }
    if (StringUtils.isNotBlank(config.getVersionColumnQualifier())) {
      this.versionColumnQualifier = Bytes.toBytes(config.getVersionColumnQualifier());
    }
  }

  @Inject
  public void setUnit(@Named("unit") TimeUnit unit) {
    this.unit = unit;
  }

  @Inject
  public void setWaitTime(@Named("waitTime") Long waitTime) {
    this.waitTime = waitTime;
  }

  @Inject
  public void setFilterConfigs(FilterConfigs<T> configs) {
    this.filterConfigs = configs.getConfigs();
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
  public byte[] getVersionColumnFamily() {
    return versionColumnFamily;
  }

  @Override
  public byte[] getVersionColumnQualifier() {
    return versionColumnQualifier;
  }

  @Override
  public long getWaitTime() {
    return waitTime;
  }

  @Override
  public TimeUnit getUnit() {
    return unit;
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

  @Override
  public byte[] getRowIdFromRow(T instance) throws IOException {
    final Object id = instance.getId();
    return getRowIdFromId((IdType) id);
  }

  @Override
  public byte[] getRowIdFromId(IdType id) throws IOException {
    final byte[] rowId;
    if (id instanceof Integer) {
      rowId = Bytes.toBytes((Integer) id);
    }
    else if (id instanceof String) {
      rowId = Bytes.toBytes((String) id);
    }
    else if (id instanceof Long) {
      rowId = Bytes.toBytes((Long) id);
    }
    else if (id instanceof Double) {
      rowId = Bytes.toBytes((Double) id);
    }
    else if (id instanceof Date) {
      rowId = Bytes.toBytes(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(((Date) id)));
    }
    else if (id instanceof Externalizable) {
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      final DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream);
      ((Externalizable) id).writeExternal(outputStream);
      IOUtils.closeQuietly(outputStream);
      rowId = byteArrayOutputStream.toByteArray();
    }
    else if (id != null) {
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(id);
      IOUtils.closeQuietly(objectOutputStream);
      IOUtils.closeQuietly(byteArrayOutputStream);
      rowId = byteArrayOutputStream.toByteArray();
    }
    else {
      rowId = new byte[0];
    }
    return rowId;
  }

  @Override
  public IdType getIdFromRowId(byte[] id) throws IOException, ClassNotFoundException {
    final Object idInstance;
    if (Externalizable.class.isAssignableFrom(idTypeClass)) {
      idInstance = provider.getInstance(idTypeClass);
      DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(id));
      ((Externalizable) idInstance).readExternal(inputStream);
    }
    else if (Integer.class.isAssignableFrom(idTypeClass)) {
      idInstance = Bytes.toInt(id);
    }
    else if (String.class.isAssignableFrom(idTypeClass)) {
      idInstance = Bytes.toString(id);
    }
    else if (Long.class.isAssignableFrom(idTypeClass)) {
      idInstance = Bytes.toLong(id);
    }
    else if (Double.class.isAssignableFrom(idTypeClass)) {
      idInstance = Bytes.toDouble(id);
    }
    else if (Date.class.isAssignableFrom(idTypeClass)) {
      try {
        idInstance = DateUtils.parseDate(Bytes.toString(id), new String[]{DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.
              getPattern()});
      }
      catch (ParseException ex) {
        throw new IOException(ex);
      }
    }
    else {
      idInstance = (IdType) new ObjectInputStream(new ByteArrayInputStream(id)).readObject();
    }
    return (IdType) idInstance;
  }
}
