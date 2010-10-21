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
import com.smartitengineering.dao.impl.hbase.spi.impl.JsonConfigLoader;
import com.smartitengineering.dao.impl.hbase.spi.impl.SchemaInfoProviderBaseConfig;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author imyousuf
 */
public class GenericBaseConfigProvider<T> implements Provider<SchemaInfoProviderBaseConfig<T>> {

  private final String classpathResourcePath;

  public GenericBaseConfigProvider(String classpathResourcePath) {
    this.classpathResourcePath = classpathResourcePath;
  }

  @Override
  public SchemaInfoProviderBaseConfig<T> get() {
    try {
      final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(classpathResourcePath);
      return JsonConfigLoader.parseJsonAsBaseConfig(resourceAsStream);
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
