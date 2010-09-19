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

import com.smartitengineering.dao.impl.hbase.spi.FilterConfigs;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author imyousuf
 */
public final class JsonConfigLoader {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static FilterConfigs parseJsonAsFilterConfigMap(InputStream inputStream) throws IOException {
    return MAPPER.readValue(inputStream, FilterConfigs.class);
  }

  public static SchemaInfoProviderBaseConfig parseJsonAsBaseConfig(InputStream inputStream) throws IOException {
    return MAPPER.readValue(inputStream, SchemaInfoProviderBaseConfig.class);
  }
}
