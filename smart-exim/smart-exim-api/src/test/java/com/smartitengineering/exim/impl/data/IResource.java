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

import com.smartitengineering.domain.annotations.Eager;
import com.smartitengineering.domain.annotations.Id;
import com.smartitengineering.domain.annotations.Name;
import com.smartitengineering.domain.annotations.ResourceDomain;
import com.smartitengineering.domain.exim.StringValueProvider;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author imyousuf
 */
@ResourceDomain(priority = IResource.PRIORITY,
                exportAsURIByDefault = true,
								exportBasicTypesInTypeElementEnabled=true)
public interface IResource
    extends StringValueProvider {

    static final int PRIORITY = 12;
    static final String RESOURCE_ID = "resourceId";
    static final String LIST = "list";
    static final String SET = "set";
    static final String SET_NAME = "setName";
    static final String COLLECTION = "collection";
    static final String MAP = "map";
    static final String ARRAY = "array";
    static final String VALID = "valid";

    @Id
    int getResourceId();

    List<Integer> getList();

    @Name(value = IResource.SET_NAME)
    Set<Long> getSet();

    Collection<Double> getCollection();

    @Eager
    Map<String, Float> getMap();

    Number[] getArray();

    boolean isValid();
}
