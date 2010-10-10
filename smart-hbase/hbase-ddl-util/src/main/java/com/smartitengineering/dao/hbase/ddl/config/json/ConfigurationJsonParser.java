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
package com.smartitengineering.dao.hbase.ddl.config.json;

import com.smartitengineering.dao.hbase.ddl.HBaseColumnFamilyConfiguration;
import com.smartitengineering.dao.hbase.ddl.HBaseTableConfiguration;
import com.smartitengineering.dao.hbase.ddl.config.HBaseColumnFamilyConfigurationImpl;
import com.smartitengineering.dao.hbase.ddl.config.HBaseTableConfigurationImpl;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public final class ConfigurationJsonParser {

  private ConfigurationJsonParser() {
    SerializationConfig config = mapper.getSerializationConfig();
    config.addMixInAnnotations(HBaseColumnFamilyConfiguration.class, HBaseColumnFamilyConfigurationImpl.class);
    config.addMixInAnnotations(HBaseTableConfiguration.class, HBaseTableConfigurationImpl.class);
    mapper.setSerializationConfig(config);
    DeserializationConfig dconfig = mapper.getDeserializationConfig();
    dconfig.addMixInAnnotations(HBaseColumnFamilyConfiguration.class, HBaseColumnFamilyConfigurationImpl.class);
    dconfig.addMixInAnnotations(HBaseTableConfiguration.class, HBaseTableConfigurationImpl.class);
    mapper.setDeserializationConfig(dconfig);
  }
  private static final ConfigurationJsonParser PARSER = new ConfigurationJsonParser();
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationJsonParser.class);
  private final ObjectMapper mapper = new ObjectMapper();

  public static Collection<HBaseTableConfiguration> getConfigurations(InputStream stream) {
    try {
      return PARSER.mapper.readValue(stream, new TypeReference<Collection<HBaseTableConfiguration>>() {
      });
    }
    catch (Exception ex) {
      LOGGER.warn("Could not parse for collection", ex);
      return Collections.emptyList();
    }
  }
}
