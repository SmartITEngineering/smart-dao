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

import com.smartitengineering.dao.common.cache.BasicKey;
import com.smartitengineering.dao.common.cache.CacheKeyPrefixStrategy;

/**
 *
 * @author imyousuf
 */
public class BasicKeyImpl<KeyId> implements BasicKey<KeyId> {
    
    private CacheKeyPrefixStrategy prefixStrategy;
    private boolean initialized;

    public void init(CacheKeyPrefixStrategy prefixStrategy) {
        if(prefixStrategy == null) {
            throw new NullPointerException();
        }
        this.prefixStrategy = prefixStrategy;
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public String getKey(KeyId key) {
        if(key == null) {
            throw new IllegalArgumentException();
        }
        if(!isInitialized()) {
            throw new IllegalStateException();
        }
        StringBuilder builder = new StringBuilder(prefixStrategy.getPrefix());
        builder.append(prefixStrategy.getPreferredPrefixIdSeparator());
        builder.append(key.toString());
        return builder.toString();
    }

}
