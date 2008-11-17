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
import com.smartitengineering.dao.common.cache.Mutex;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author imyousuf
 */
public class MutexImpl<KeyType extends Object>
    implements Mutex<KeyType> {

    private Map<KeyType, Semaphore> lockMap;
    private Map<Thread, Lock<KeyType>> threadLocks;
    private Map<KeyType, Long> idleDuration;
    private Map<KeyType, String> expirationPolicy;
    private int concurrentPermits;
    private long defaultWaitInMillisecond;
    private String expiryPolicy;
    private long expirationDuration;
    private Timer timer;

    protected MutexImpl() {
        idleDuration = new Hashtable<KeyType, Long>();
        lockMap = new MonitorableMap<KeyType, Semaphore>(
            new WeakHashMap<KeyType, Semaphore>(), new IdleTimeMapMonitor());
        threadLocks = new WeakHashMap<Thread, Lock<KeyType>>();
        setConcurrentPermissions(1000);
        setDefaultWaitTimeoutInMillisecond(2000);
        setExpiryPeriod(30000);
        initExpirationExecutor();
    }

    public Lock acquire(KeyType key)
        throws InterruptedException {
        return acquire(key, getDefaultWaitTimeoutInMillisecond());
    }

    public int getConcurrentPermissions() {
        return concurrentPermits;
    }

    public void setConcurrentPermissions(int permissions) {
        concurrentPermits = permissions;
    }

    public Lock<KeyType> acquire(KeyType key,
                                 long waitTimeout)
        throws InterruptedException {
        if (key == null) {
            return null;
        }
        final int eConcurrentPermits = getConcurrentPermissions();
        Lock eLock = null;
        String currentExpirationPolicy = null;
        synchronized (expiryPolicy) {
            currentExpirationPolicy = getExpiryPolicy();
        }
        expirationPolicy.put(key, currentExpirationPolicy);
        if (lockMap.containsKey(key)) {
            boolean acquired = lockMap.get(key).tryAcquire(1,
                getDefaultWaitTimeoutInMillisecond(), TimeUnit.MILLISECONDS);
            if (acquired) {
                eLock = CacheAPIFactory.getLock(1, key);
            }
        }
        else {
            boolean eAdded;
            synchronized (this) {
                eAdded = lockMap.containsKey(key);
                if (!eAdded) {
                    Semaphore eSemaphore = new Semaphore(eConcurrentPermits,
                        true);
                    lockMap.put(key, eSemaphore);
                    int eLockedPermissions = eSemaphore.drainPermits();
                    eLock = CacheAPIFactory.getLock(eLockedPermissions, key);
                }
            }
            if (eAdded) {
                boolean acquired = lockMap.get(key).tryAcquire(1,
                    getDefaultWaitTimeoutInMillisecond(), TimeUnit.MILLISECONDS);
                if (acquired) {
                    eLock = CacheAPIFactory.getLock(1, key);
                }
            }
        }
        if (eLock != null) {
            threadLocks.put(Thread.currentThread(), eLock);
        }
        return eLock;
    }

    public void release(Lock<KeyType> lock) {
        if (lock == null) {
            lock = getAttainedLockForCurrentThread();
            if (lock == null) {
                return;
            }
        }
        if (lockMap.containsKey(lock.getKey())) {
            KeyType eKey = lock.getKey();
            Semaphore eSemaphore = lockMap.get(eKey);
            int ePermissions = lock.getNumberOfPermissionsAttained();
            eSemaphore.release(ePermissions);
        }
    }

    public void setDefaultWaitTimeoutInMillisecond(long defaultWait) {
        defaultWaitInMillisecond = defaultWait;
    }

    public long getDefaultWaitTimeoutInMillisecond() {
        return defaultWaitInMillisecond;
    }

    public Lock<KeyType> getAttainedLockForCurrentThread() {
        return threadLocks.get(Thread.currentThread());
    }

    public void expire(KeyType key) {
        lockMap.remove(key);
    }

    public void setExpiryPeriod(long durationInMillisecond) {
        expirationDuration = durationInMillisecond;
    }

    public long getExpiryPeriod() {
        return expirationDuration;
    }

    public void setExpiryPolicy(String policy) {
        String newExpirationPolicy;
        try {
            ExpiryPolicy.valueOf(policy);
            newExpirationPolicy = policy;
        }
        catch (IllegalArgumentException argumentException) {
            newExpirationPolicy = ExpiryPolicy.SIMPLE_TIME_TRIGGERED_EXPIRATION.
                name();
        }
        synchronized (expiryPolicy) {
            expiryPolicy = newExpirationPolicy;
        }
    }

    public String getExpiryPolicy() {
        if (expiryPolicy == null) {
            expiryPolicy = ExpiryPolicy.SIMPLE_TIME_TRIGGERED_EXPIRATION.name();
        }
        return expiryPolicy;
    }

    protected void initExpirationExecutor() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new ExpireExecutor(), expirationDuration,
            expirationDuration / 4);
    }

    private class IdleTimeMapMonitor
        implements MapMonitor<KeyType, Semaphore> {

        public void notifyPut(KeyType key,
                              Semaphore value) {
            idleDuration.put(key, System.currentTimeMillis());
        }

        public void notifyGet(Object key) {
            if (ExpiryPolicy.IDLE_FOR_DURATION.name().equals(expirationPolicy.
                get(key))) {
                idleDuration.put((KeyType) key, System.currentTimeMillis());
            }
        }

        public void notifyPutAll(
            Map<? extends KeyType, ? extends Semaphore> values) {
            throw new UnsupportedOperationException();
        }

        public void notifyRemove(Object key) {
            idleDuration.remove(key);
            expirationPolicy.remove(key);
        }
    }

    private class ExpireExecutor
        extends TimerTask
        implements Runnable {

        public void run() {
            Set<Map.Entry<KeyType, Long>> entries = idleDuration.entrySet();
            for (Map.Entry<KeyType, Long> entry : entries) {
                if ((System.currentTimeMillis() -
                    entry.getValue().longValue()) >=
                    getExpiryPeriod()) {
                    expire(entry.getKey());
                }
            }
        }
    }
}
