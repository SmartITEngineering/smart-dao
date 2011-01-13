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
package com.smartitengineering.dao.common.cache.impl;

import com.google.inject.Inject;
import com.smartitengineering.dao.common.cache.CacheKeyPrefixStrategy;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class SimpleCacheKeyPrefixStrategyImpl
    implements CacheKeyPrefixStrategy {

  private static final String DEFAULT_SEPARATOR = ":";
  @Inject
  private String prefix;
  @Inject(optional = true)
  private String preferredPrefixIdSeparator;

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getPreferredPrefixIdSeparator() {
    return StringUtils.isBlank(preferredPrefixIdSeparator) ? DEFAULT_SEPARATOR : preferredPrefixIdSeparator;
  }

  protected void setPreferredPrefixIdSeparator(String preferredPrefixIdSeparator) {
    this.preferredPrefixIdSeparator = preferredPrefixIdSeparator;
  }

  protected void setPrefix(String prefix) {
    this.prefix = prefix;
  }
}
