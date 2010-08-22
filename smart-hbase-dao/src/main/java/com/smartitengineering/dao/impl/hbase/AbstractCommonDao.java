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
package com.smartitengineering.dao.impl.hbase;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.impl.hbase.spi.ObjectRowConverter;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import com.smartitengineering.domain.PersistentDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class AbstractCommonDao<Template extends PersistentDTO> implements CommonReadDao<Template>,
                                                                          CommonWriteDao<Template> {

  public static final int DEFAULT_MAX = 1000;
  private ObjectRowConverter<Template> converter;
  private SchemaInfoProvider infoProvider;
  private Configuration configuration;
  private HTablePool tablePool;

  public ObjectRowConverter<Template> getConverter() {
    return converter;
  }

  public void setConverter(ObjectRowConverter<Template> converter) {
    this.converter = converter;
  }

  public SchemaInfoProvider getInfoProvider() {
    return infoProvider;
  }

  public void setInfoProvider(SchemaInfoProvider infoProvider) {
    this.infoProvider = infoProvider;
  }

  public Configuration getConfiguration() {
    if (configuration == null) {
      configuration = HBaseConfiguration.create();
    }
    return configuration;
  }

  public HTablePool getTablePool() {
    if (tablePool == null) {
      tablePool = new HTablePool(getConfiguration(), DEFAULT_MAX);
    }
    return tablePool;
  }

  /*
   * READ OPERATIONS
   */

  /*
   * Unsupported read operations
   */
  @Override
  public Set<Template> getAll() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <OtherTemplate> OtherTemplate getOther(List<QueryParameter> query) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <OtherTemplate> List<OtherTemplate> getOtherList(List<QueryParameter> query) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /*
   * Supported read operations
   */
  @Override
  public Set<Template> getByIds(List<Integer> ids) {
    LinkedHashSet<Template> set = new LinkedHashSet<Template>(ids.size());
    for (Integer id : ids) {
      set.add(getById(id));
    }
    return set;
  }

  @Override
  public Template getById(Integer id) {
    HTableInterface hTable = null;
    try {
      Get get = new Get(Bytes.toBytes(id));
      hTable = getTablePool().getTable(infoProvider.getMainTableName());
      Result result =
             hTable.get(get);
      return getConverter().rowsToObject(result);
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    finally {
      try {
        if (hTable != null) {
          getTablePool().putTable(hTable);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  public Template getSingle(List<QueryParameter> query) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Template> getList(List<QueryParameter> query) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Template getSingle(QueryParameter... query) {
    return getSingle(Arrays.asList(query));
  }

  @Override
  public List<Template> getList(QueryParameter... query) {
    return getList(Arrays.asList(query));
  }

  @Override
  public <OtherTemplate> OtherTemplate getOther(QueryParameter... query) {
    return this.<OtherTemplate>getOther(Arrays.asList(query));
  }

  @Override
  public <OtherTemplate> List<OtherTemplate> getOtherList(QueryParameter... query) {
    return this.<OtherTemplate>getOtherList(Arrays.asList(query));
  }

  /*
   * WRITE OPERATIONS
   */
  @Override
  public void save(Template... states) {
    LinkedHashMap<String, List<Put>> allPuts = new LinkedHashMap<String, List<Put>>();
    for (Template state : states) {
      LinkedHashMap<String, Put> puts = getConverter().objectToRows(state);
      for (Map.Entry<String, Put> put : puts.entrySet()) {
        final List<Put> putList;
        if (allPuts.containsKey(put.getKey())) {
          putList = allPuts.get(put.getKey());
        }
        else {
          putList = new ArrayList<Put>();
          allPuts.put(put.getKey(), putList);
        }
        putList.add(put.getValue());
      }
    }
    for (Map.Entry<String, List<Put>> puts : allPuts.entrySet()) {
      HTableInterface hTable = null;
      try {
        hTable = getTablePool().getTable(puts.getKey());
        hTable.put(puts.getValue());
      }
      catch (Exception ex) {
        throw new RuntimeException(ex);
      }
      finally {
        try {
          if (hTable != null) {
            getTablePool().putTable(hTable);
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  @Override
  public void update(Template... states) {
    save(states);
  }

  @Override
  public void delete(Template... states) {
    LinkedHashMap<String, List<Delete>> allPuts = new LinkedHashMap<String, List<Delete>>();
    for (Template state : states) {
      LinkedHashMap<String, Delete> puts = getConverter().objectToDeleteableRows(state);
      for (Map.Entry<String, Delete> put : puts.entrySet()) {
        final List<Delete> putList;
        if (allPuts.containsKey(put.getKey())) {
          putList = allPuts.get(put.getKey());
        }
        else {
          putList = new ArrayList<Delete>();
          allPuts.put(put.getKey(), putList);
        }
        putList.add(put.getValue());
      }
    }
    for (Map.Entry<String, List<Delete>> puts : allPuts.entrySet()) {
      HTableInterface hTable = null;
      try {
        hTable = getTablePool().getTable(puts.getKey());
        hTable.delete(puts.getValue());
      }
      catch (Exception ex) {
        throw new RuntimeException(ex);
      }
      finally {
        try {
          if (hTable != null) {
            getTablePool().putTable(hTable);
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
