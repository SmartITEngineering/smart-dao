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

import com.smartitengineering.dao.common.cache.CacheEventRegistrar;
import com.smartitengineering.dao.common.cache.ChangeEvent;
import com.smartitengineering.dao.common.cache.ChangeEvent.ChangeType;
import com.smartitengineering.dao.common.cache.ChangeListener;
import com.smartitengineering.domain.PersistentDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author imyousuf
 */
public class CacheEventRegistrarImpl
    implements CacheEventRegistrar {

    private static final String ALL = "ALL";
    private Map<Class, Set<ListenerTuple>> observers;
    private Map<ChangeListener, Set<Class>> listeners;

    {
        observers = new HashMap<Class, Set<ListenerTuple>>();
        listeners = new HashMap<ChangeListener, Set<Class>>();
    }
    
    public void addCacheEventListeners(Class<? extends PersistentDTO> observe,
                                       ChangeType type,
                                       ChangeListener... listeners)
        throws IllegalArgumentException {
        if (observe == null || listeners == null || listeners.length <= 0) {
            throw new IllegalArgumentException();
        }
        for(ChangeListener listener : listeners) {
            ListenerTuple tuple = new ListenerTuple();
            tuple.setListener(listener);
            tuple.setType(type);
            Set<ListenerTuple> tuples;
            if(!observers.containsKey(observe)) {
                tuples = new LinkedHashSet<ListenerTuple>();
                observers.put(observe, tuples);
            }
            else {
                tuples = observers.get(observe);
            }
            tuples.add(tuple);
            Set<Class> classes;
            if(!this.listeners.containsKey(listener)) {
                classes = new LinkedHashSet<Class>();
                this.listeners.put(listener, classes);
            }
            else {
                classes = this.listeners.get(listener);
            }
            classes.add(observe);
        }
    }

    public void removeListeners(ChangeListener... listeners) {
        for(ChangeListener listener : listeners) {
            if(this.listeners.containsKey(listener)) {
                Set<Class> classes = this.listeners.get(listener);
                if(classes != null && !classes.isEmpty()) {
                    for(Class observe : classes) {
                        Set<ListenerTuple> tuples = observers.get(observe);
                        if(tuples != null && !tuples.isEmpty()) {
                            Iterator<ListenerTuple> tupleIterator = tuples.iterator();
                            while(tupleIterator.hasNext()) {
                                ListenerTuple tuple = tupleIterator.next();
                                if(tuple.getListener() != null && tuple.getListener() == listener) {
                                    tuples.remove(tuple);
                                }
                            }
                        }
                    }
                }
                this.listeners.remove(listener);
            }
        }
    }

    public void fireEvent(ChangeEvent event) {
        Class observe = event.getSource().getClass();
        if(observers.containsKey(observe)) {
            Set<ListenerTuple> tuples = observers.get(observe);
            ArrayList<String> types = new ArrayList<String>();
            types.add(ALL);
            if(event.getChangeType() != null) {
                types.add(event.getChangeType().name());
            }
            if(tuples != null && !tuples.isEmpty()) {
                for(ListenerTuple tuple : tuples) {
                    if(types.contains(tuple.getType())) {
                        ChangeListener listener = tuple.getListener();
                        if(listener != null) {
                            listener.cacheChanged(event);
                        }
                    }
                }
            }
        }
    }

    private class ListenerTuple {

        private ChangeListener listener;
        private String type;

        public ChangeListener getListener() {
            return listener;
        }

        public void setListener(ChangeListener listener) {
            this.listener = listener;
        }

        public String getType() {
            return type != null ? type : "";
        }

        public void setType(ChangeType type) {
            if (type != null) {
                this.type = type.name();
            }
            else {
                this.type = ALL;
            }
        }

        public void setType(String type) {
            if (type != null) {
                this.type = type;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ListenerTuple other = (ListenerTuple) obj;
            if (this.listener != other.listener &&
                (this.listener == null || !this.listener.equals(other.listener))) {
                return false;
            }
            if (!this.type.equals(other.type)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash =
                29 * hash +
                (this.listener != null ? this.listener.hashCode() : 0);
            hash = 29 * hash + (this.type != null ? this.type.hashCode() : 0);
            return hash;
        }
    }
}
