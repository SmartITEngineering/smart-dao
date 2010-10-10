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
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.io.hfile.Compression.Algorithm;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 *
 * @author imyousuf
 */
@JsonDeserialize(as = HBaseColumnFamilyConfigurationImpl.class)
public class HBaseColumnFamilyConfigurationImpl implements HBaseColumnFamilyConfiguration {

  private String familyName,
      bloomFilterType = HColumnDescriptor.DEFAULT_BLOOMFILTER,
      compressionType = Algorithm.NONE.name();
  private boolean blockCacheEnabled = HColumnDescriptor.DEFAULT_BLOCKCACHE,
      inMemory = HColumnDescriptor.DEFAULT_IN_MEMORY;
  private int scope = HColumnDescriptor.DEFAULT_REPLICATION_SCOPE,
      blockSize = HColumnDescriptor.DEFAULT_BLOCKSIZE,
      timeToLive = HColumnDescriptor.DEFAULT_TTL,
      maxVersions = HColumnDescriptor.DEFAULT_VERSIONS;

  @Override
  public String getFamilyName() {
    return familyName;
  }

  @Override
  public int getScope() {
    return scope;
  }

  @Override
  public boolean isBlockCacheEnabled() {
    return blockCacheEnabled;
  }

  @Override
  public int getBlockSize() {
    return blockSize;
  }

  @Override
  public int getTimeToLive() {
    return timeToLive;
  }

  @Override
  public String getBloomFilterType() {
    return bloomFilterType;
  }

  @Override
  public String getCompressionType() {
    return compressionType;
  }

  @Override
  public boolean isInMemory() {
    return inMemory;
  }

  @Override
  public int getMaxVersions() {
    return maxVersions;
  }

  public void setBlockCacheEnabled(boolean blockCacheEnabled) {
    this.blockCacheEnabled = blockCacheEnabled;
  }

  public void setBlockSize(int blockSize) {
    this.blockSize = blockSize;
  }

  public void setBloomFilterType(String bloomFilterType) {
    this.bloomFilterType = bloomFilterType;
  }

  public void setCompressionType(String compressionType) {
    this.compressionType = compressionType;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public void setInMemory(boolean inMemory) {
    this.inMemory = inMemory;
  }

  public void setMaxVersions(int maxVersions) {
    this.maxVersions = maxVersions;
  }

  public void setScope(int scope) {
    this.scope = scope;
  }

  public void setTimeToLive(int timeToLive) {
    this.timeToLive = timeToLive;
  }
}
