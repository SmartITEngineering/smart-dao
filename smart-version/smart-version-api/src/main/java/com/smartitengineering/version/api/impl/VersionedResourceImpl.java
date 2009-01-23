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
import com.smartitengineering.version.api.VersionedResource;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author imyousuf
 */
public class VersionedResourceImpl
    implements VersionedResource {

    private Resource headVersionResource;
    private Collection<Revision> revisions;

    /**
     * Get the value of revisions
     *
     * @return the value of revisions
     */
    public Collection<Revision> getRevisions() {
        if (revisions == null) {
            return Collections.emptySet();
        }
        return revisions;
    }

    /**
     * Set the value of revisions
     *
     * @param revisions new value of revisions
     */
    public void setRevisions(Collection<Revision> revisions) {
        this.revisions = revisions;
    }

    /**
     * Get the value of headVersionResource
     *
     * @return the value of headVersionResource
     */
    public Resource getHeadVersionResource() {
        return headVersionResource;
    }

    /**
     * Set the value of headVersionResource
     *
     * @param headVersionResource new value of headVersionResource
     */
    public void setHeadVersionResource(Resource headVersionResource) {
        this.headVersionResource = headVersionResource;
    }
}
