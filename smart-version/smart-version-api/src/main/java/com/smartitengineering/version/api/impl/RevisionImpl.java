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
package com.smartitengineering.version.api.impl;

import com.smartitengineering.version.api.Resource;
import com.smartitengineering.version.api.Revision;

/**
 *
 * @author imyousuf
 */
public class RevisionImpl
    implements Revision {

    private String revisionId;
    private Resource resource;

    /**
     * Get the value of resource
     *
     * @return the value of resource
     */
    public Resource getResource() {
        if (resource == null) {
            throw new IllegalStateException(
                "Resource must be set before attempting to retrieve!");
        }
        return resource;
    }

    /**
     * Set the value of resource
     *
     * @param resource new value of resource
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * Get the value of revisionId
     *
     * @return the value of revisionId
     */
    public String getRevisionId() {
        return revisionId;
    }

    /**
     * Set the value of revisionId
     *
     * @param revisionId new value of revisionId
     */
    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }
}
