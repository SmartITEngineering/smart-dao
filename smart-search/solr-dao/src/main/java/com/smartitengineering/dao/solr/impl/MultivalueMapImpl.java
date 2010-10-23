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
package com.smartitengineering.dao.solr.impl;

import com.smartitengineering.dao.solr.MultivalueMap;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author imyousuf
 */
public class MultivalueMapImpl<K, V> extends AbstractMap<K, List<V>> implements MultivalueMap<K, V> {

  private ConcurrentHashMap<K, List<V>> map = new ConcurrentHashMap<K, List<V>>();

  @Override
  public Set<Entry<K, List<V>>> entrySet() {
    return map.entrySet();
  }

  @Override
  public List<V> put(K key, List<V> value) {
    return map.put(key, value);
  }

  @Override
  public void addValue(K key, V value) {
    List<V> list = map.get(key);
    if (list == null) {
      list = new ArrayList<V>();
      final List<V> putIfAbsent = map.putIfAbsent(key, list);
      if (putIfAbsent != null) {
        list = putIfAbsent;
      }
    }
    list.add(value);
  }

  @Override
  public <T extends K> V getFirst(K key) {
    List<V> value = map.get(key);
    if (value != null && !value.isEmpty()) {
      return value.get(0);
    }
    return null;
  }
}
