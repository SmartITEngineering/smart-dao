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
package com.smartitengineering.version.api;

import java.util.Collection;

/**
 * This object represents an resource that is versioned. It provides easy access
 * to versions of the resource.
 * @author imyousuf
 */
public interface VersionedResource {

    /**
     * Retrieve the content of object from the HEAD revision.
     * @return Content of HEAD
     */
    public Resource getHeadVersionResource();

    /**
     * Get the versions of the object. The must be sorted in descending order,
     * i.e. HEAD will be in index 0 and the first version @ 'size - 1'th
     * position. 
     * @return All versions for the object, empty if now revisions available.
     */
    public Collection<Revision> getRevisions();
}
