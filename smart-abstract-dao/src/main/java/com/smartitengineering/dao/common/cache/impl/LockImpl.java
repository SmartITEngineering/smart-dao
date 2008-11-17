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

import com.smartitengineering.dao.common.cache.Lock;

/**
 *
 * @author imyousuf
 */
public class LockImpl<KeyType extends Object>
    implements Lock<KeyType> {

    private int permissionAttained;
    private KeyType lockKey;

    protected void setLockKey(KeyType lockKey) {
        this.lockKey = lockKey;
    }

    protected void setAttainedPermissions(int permissionAttained) {
        if (permissionAttained <= 0) {
            throw new IllegalArgumentException();
        }
        this.permissionAttained = permissionAttained;
    }

    public KeyType getKey() {
        return lockKey;
    }

    public int getNumberOfPermissionsAttained() {
        return permissionAttained;
    }
}
