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
package com.smartitengineering.dao.impl.hbase.spi.impl.guice;

import com.google.inject.Provider;
import com.smartitengineering.dao.impl.hbase.spi.FilterConfigs;
import com.smartitengineering.dao.impl.hbase.spi.impl.JsonConfigLoader;
import java.io.IOException;

/**
 *
 * @author imyousuf
 */
public class GenericFilterConfigsProvider<T> implements Provider<FilterConfigs<T>> {

  private final String classpathResource;

  public GenericFilterConfigsProvider(String classpathResource) {
    this.classpathResource = classpathResource;
  }

  @Override
  public FilterConfigs<T> get() {
    try {
      return JsonConfigLoader.parseJsonAsFilterConfigMap(getClass().getClassLoader().getResourceAsStream(
          classpathResource));
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
