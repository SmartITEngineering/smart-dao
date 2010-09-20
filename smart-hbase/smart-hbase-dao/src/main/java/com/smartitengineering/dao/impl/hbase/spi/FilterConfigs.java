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
package com.smartitengineering.dao.impl.hbase.spi;

import com.smartitengineering.dao.impl.hbase.spi.impl.FilterConfigImpl;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 *
 * @author imyousuf
 */
public class FilterConfigs<Template> {

  private Map<String, FilterConfig> configs;

  @JsonDeserialize(contentAs = FilterConfigImpl.class, as = LinkedHashMap.class)
  public void setConfigs(Map<String, FilterConfig> configs) {
    this.configs = configs;
  }

  public Map<String, FilterConfig> getConfigs() {
    return configs;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FilterConfigs other = (FilterConfigs) obj;
    if (this.configs != other.configs && (this.configs == null || !this.configs.equals(other.configs))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 43 * hash + (this.configs != null ? this.configs.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "FilterConfigs{" + "configs=" + configs + '}';
  }
}
