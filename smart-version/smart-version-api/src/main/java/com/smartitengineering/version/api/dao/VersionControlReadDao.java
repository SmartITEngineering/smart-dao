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
package com.smartitengineering.version.api.dao;

import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.version.api.Commit;
import com.smartitengineering.version.api.Resource;
import com.smartitengineering.version.api.Revision;
import com.smartitengineering.version.api.VersionedResource;
import java.util.Collection;

/**
 * Its the main interface through which all CRUD opertation to the RCS will be
 * made. Service providers will mainly implement this and provide access to
 * users.
 * @author imyousuf
 */
public interface VersionControlReadDao {

    /**
     * Get a resource with its versions specified by the resource id.
     * @param resourceId Unique identifier of the resource
     * @return The versioned representation of the resource.
     */
    public VersionedResource getVersionedResource(final String resourceId);

    /**
     * Retrieve the HEAD version of the specified resource id.
     * @param resourceId The resource's id to fetch
     * @return Fetched resource for id
     */
    public Resource getResource(final String resourceId);

    /**
     * Retrieve the specified revision of the specified resource
     * @param revisionId The revision to get
     * @param resourceId The resource to get
     * @return The resource with the specified resource id and revision id
     */
    public Resource getResourceByRevision(final String revisionId,
                                          final String resourceId);

    /**
     * Search for commits. Please implementation documentation to check whether
     * this operation is supported or not and how to use it.
     * @param parameters Parameters to be used for search
     * @return Search result or empty collection if nothing could be matched
     */
    public Collection<Commit> searchForCommits(
        final Collection<QueryParameter> parameters);

    /**
     * Search for VersionedObjects. Please implementation documentation to check
     * whether this operation is supported or not and how to use it.
     * @param parameters Parameters to be used fo search
     * @return Search result or empty collection if nothing could be matched
     */
    public Collection<Revision> searchForRevisions(
        final Collection<QueryParameter> parameters);
}
