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
import com.smartitengineering.dao.common.cache.ChangeEvent;
import com.smartitengineering.dao.common.cache.Lock;
import com.smartitengineering.dao.common.cache.Mutex;
import com.smartitengineering.domain.PersistentDTO;

/**
 *
 * @author imyousuf
 */
public final class CacheAPIFactory {

    private CacheAPIFactory() {
        throw new AssertionError();
    }

    public static <KeyType> Lock<KeyType> getLock(int attaibnedPermissions,
                                                  KeyType key) {
        LockImpl<KeyType> lockImpl = new LockImpl<KeyType>();
        lockImpl.setLockKey(key);
        lockImpl.setAttainedPermissions(attaibnedPermissions);
        return lockImpl;
    }

    public static <LockKeyType> Mutex<LockKeyType> getMutex() {
        return new MutexImpl<LockKeyType>();
    }

    public static <K> BasicKey<K> getBasicKey(String prefix,
                                              String prefixSeparator) {
        BasicKey<K> basicKeyImpl = new BasicKeyImpl<K>();
        basicKeyImpl.init(getSimpleCacheKeyPrefixStrategy(prefix,
            prefixSeparator));
        return basicKeyImpl;
    }

    public static CacheKeyPrefixStrategy getSimpleCacheKeyPrefixStrategy(
        String prefix,
        String prefixSeparator) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        SimpleCacheKeyPrefixStrategyImpl prefixStrategyImpl =
            new SimpleCacheKeyPrefixStrategyImpl();
        prefixStrategyImpl.setPrefix(prefix);
        prefixStrategyImpl.setPreferredPrefixIdSeparator(prefixSeparator != null
            ? prefixSeparator : ":");
        return prefixStrategyImpl;
    }

    public static <P extends PersistentDTO> ChangeEvent getChangeEvent(P source,
                                                                       ChangeEvent.ChangeType changeType) {
        ChangeEventImpl eventImpl = new ChangeEventImpl();
        eventImpl.setChangeType(changeType);
        eventImpl.setSource(source);
        return eventImpl;
    }
}
