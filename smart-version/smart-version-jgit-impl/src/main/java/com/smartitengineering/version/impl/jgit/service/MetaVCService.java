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
package com.smartitengineering.version.impl.jgit.service;

import com.smartitengineering.version.api.Commit;

/**
 * The API for alternate storage access of versioning meta info. It will be used
 * in conjunction with Various RCS Implementations. Its basic intention is to
 * make search easier and faster.
 * @author imyousuf
 */
public interface MetaVCService {

    /**
     * Save resurces in a commit; it could also trigger update of resources and
     * thus updating their head revision
     * @param commit To persist and synch
     */
    public void saveResources(Commit commit);

    /**
     * Delete resources from system denoted by the commit
     * @param commit To delete resource referred from
     */
    public void deleteResources(Commit commit);

    /**
     * Get all version ids of the resource denoted by resource id.
     * @param resourceId The resource of which versions are to be retrieved
     * @return Descendending sorted versions, that is HEAD at index 0
     */
    public String[] getVersionsForResource(String resourceId);

    /**
     * Retrieve the revesion ID of the head (latest version) of the resource.
     * @param resourceId Resource for which to fetch the latest head revision
     * @return Head revision id of the resource
     */
    public String getHeadVersionForResource(String resourceId);
}
