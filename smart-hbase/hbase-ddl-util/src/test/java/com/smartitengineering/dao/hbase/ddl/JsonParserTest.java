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

import com.smartitengineering.dao.hbase.ddl.config.json.ConfigurationJsonParser;
import java.io.InputStream;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class JsonParserTest {

  @Test
  public void testParsing() {
    InputStream classpathResource = getClass().getClassLoader().getResourceAsStream("ddl-config-sample1.json");
    Assert.assertNotNull(classpathResource);
    Collection<HBaseTableConfiguration> configs = ConfigurationJsonParser.getConfigurations(classpathResource);
    Assert.assertNotNull(configs);
    Assert.assertEquals(1, configs.size());
    HBaseTableConfiguration configuration = configs.iterator().next();
    Assert.assertNotNull(configuration);
    Assert.assertEquals("test", configuration.getTableName());
    Assert.assertNotNull(configuration.getColumnFamilyConfigurations());
    Assert.assertEquals(1, configuration.getColumnFamilyConfigurations().size());
    Assert.assertEquals("family1", configuration.getColumnFamilyConfigurations().iterator().next().getFamilyName());
  }
}
