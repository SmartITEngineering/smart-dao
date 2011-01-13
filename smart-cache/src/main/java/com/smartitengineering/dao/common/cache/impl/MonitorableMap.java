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
    if (decoratedMap == null || monitor == null) {
      throw new NullPointerException();
    }
    mapToDecorate = decoratedMap;
    mapMonitor = monitor;
  }

  @Override
  public int size() {
    return mapToDecorate.size();
  }

  @Override
  public boolean isEmpty() {
    return mapToDecorate.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return mapToDecorate.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return mapToDecorate.containsValue(value);
  }

  @Override
  public V get(Object key) {
    mapMonitor.notifyGet((K) key);
    return mapToDecorate.get(key);
  }

  @Override
  public V put(K key,
               V value) {
    mapMonitor.notifyPut(key, value);
    return mapToDecorate.put(key, value);
  }

  @Override
  public V remove(Object key) {
    mapMonitor.notifyRemove((K) key);
    return mapToDecorate.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    mapMonitor.notifyPutAll(m);
    mapToDecorate.putAll(m);
  }

  @Override
  public void clear() {
    mapToDecorate.clear();
  }

  @Override
  public Set<K> keySet() {
    return mapToDecorate.keySet();
  }

  @Override
  public Collection<V> values() {
    return mapToDecorate.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return mapToDecorate.entrySet();
  }
}
