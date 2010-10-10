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
package com.smartitengineering.dao.hbase.ddl;

import java.io.IOException;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.hfile.Compression.Algorithm;
import org.apache.hadoop.hbase.regionserver.StoreFile.BloomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class HBaseTableGenerator {

  private final Collection<HBaseTableConfiguration> configurations;
  private final Configuration hBaseConfiguration;
  private final boolean recreate;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public HBaseTableGenerator(Collection<HBaseTableConfiguration> configurations, Configuration hBaseConfiguration,
                             boolean recreate) {
    if (configurations == null || hBaseConfiguration == null) {
      throw new IllegalArgumentException("Configurations or HBaseConfiguration can not be null!");
    }
    this.configurations = configurations;
    this.hBaseConfiguration = hBaseConfiguration;
    this.recreate = recreate;
  }

  public void generateTables() throws MasterNotRunningException, IOException {
    HBaseAdmin admin = new HBaseAdmin(hBaseConfiguration);
    for (HBaseTableConfiguration configuration : configurations) {
      if (recreate && admin.tableExists(configuration.getTableName())) {
        admin.deleteTable(configuration.getTableName());
      }
      if (!admin.tableExists(configuration.getTableName())) {
        HTableDescriptor descriptor = new HTableDescriptor(configuration.getTableName());
        for (HBaseColumnFamilyConfiguration familyConfiguration : configuration.getColumnFamilyConfigurations()) {
          final HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(familyConfiguration.getFamilyName());
          hColumnDescriptor.setBlockCacheEnabled(familyConfiguration.isBlockCacheEnabled());
          final int blockSize = familyConfiguration.getBlockSize();
          if (blockSize > -1) {
            hColumnDescriptor.setBlocksize(blockSize);
          }
          final String bloomFilterType = familyConfiguration.getBloomFilterType();
          if (StringUtils.isNotBlank(bloomFilterType)) {
            hColumnDescriptor.setBloomFilterType(BloomType.valueOf(bloomFilterType));
          }
          final String compressionType = familyConfiguration.getCompressionType();
          if (StringUtils.isNotBlank(compressionType)) {
            hColumnDescriptor.setCompressionType(Algorithm.valueOf(compressionType));
          }
          hColumnDescriptor.setInMemory(familyConfiguration.isInMemory());
          final int maxVersions = familyConfiguration.getMaxVersions();
          if (maxVersions > -1) {
            hColumnDescriptor.setMaxVersions(maxVersions);
          }
          final int scope = familyConfiguration.getScope();
          if (scope > 0) {
            hColumnDescriptor.setScope(scope);
          }
          final int timeToLive = familyConfiguration.getTimeToLive();
          if (timeToLive > 0) {
            hColumnDescriptor.setTimeToLive(timeToLive);
          }
          descriptor.addFamily(hColumnDescriptor);
        }
        admin.createTable(descriptor);
      }
    }
  }
}
