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
package com.smartitengineering.version.impl.jgit;

import java.io.IOException;
import java.util.Map;

/**
 * A JGit DAO Extension to support reading blob objects from repository for git
 * object id.
 * @author imyousuf
 */
public interface JGitDaoExtension {

    /**
     * Read blob objects from git repository by their objectId of git (not their
     * path)
     * @param objectIds Ids of object to read.
     * @return Blobs represented by their objectId as key
     * @throws java.io.IOException If and only if JGit throws it
     * @throws java.lang.IllegalArgumentException If any object id of the objectIds isn't
     *                                  a blob's id
     */
    public Map<String, byte[]> readBlobObjects(String... objectIds)
        throws IOException,
               IllegalArgumentException;

    /**
     * 
     * @param objectId
     * @return
     * @throws java.io.IOException If and only if JGit throws it
     * @throws java.lang.IllegalArgumentException If object id  isn't a blob's
     *                                            id
     */
    public byte[] readObject(String objectId)
        throws IOException,
               IllegalArgumentException;
}
