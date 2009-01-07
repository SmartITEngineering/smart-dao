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

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author imyousuf
 */
public class MonitorableMap<K, V> implements Map<K, V> {
    
    private Map<K, V> mapToDecorate;
    
    private MapMonitor<K, V> mapMonitor;
    
    protected MonitorableMap(Map<K, V> decoratedMap, MapMonitor<K, V> monitor) {
        if(decoratedMap == null || monitor == null) {
            throw new NullPointerException();
        }
        mapToDecorate = decoratedMap;
        mapMonitor = monitor;
    }

    public int size() {
        return mapToDecorate.size();
    }

    public boolean isEmpty() {
        return mapToDecorate.isEmpty();
    }

    public boolean containsKey(Object key) {
        return mapToDecorate.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return mapToDecorate.containsValue(value);
    }

    public V get(Object key) {
        mapMonitor.notifyGet(key);
        return mapToDecorate.get(key);
    }

    public V put(K key,
                 V value) {
        mapMonitor.notifyPut(key, value);
        return mapToDecorate.put(key, value);
    }

    public V remove(Object key) {
        mapMonitor.notifyRemove(key);
        return mapToDecorate.remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        mapMonitor.notifyPutAll(m);
        mapToDecorate.putAll(m);
    }

    public void clear() {
        mapToDecorate.clear();
    }

    public Set<K> keySet() {
        return mapToDecorate.keySet();
    }

    public Collection<V> values() {
        return mapToDecorate.values();
    }

    public Set<Entry<K, V>> entrySet() {
        return mapToDecorate.entrySet();
    }

}
