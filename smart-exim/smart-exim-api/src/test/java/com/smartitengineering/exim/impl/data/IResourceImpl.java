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
package com.smartitengineering.exim.impl.data;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author imyousuf
 */
public class IResourceImpl implements IResource {

    public int getResourceId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Integer> getList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Long> getSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Double> getCollection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, Float> getMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Number[] getArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String format() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parse(String value)
        throws ParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
