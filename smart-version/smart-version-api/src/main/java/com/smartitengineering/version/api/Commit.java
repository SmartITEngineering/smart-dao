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
import java.util.Date;

/**
 * It represents a change in the system.
 * @author imyousuf
 */
public interface Commit {

    /**
     * Get the revisions that have changed in the commit.
     * @return Changes of the commit
     */
    public Collection<Revision> getRevisions();

    /**
     * Retrieves the unique identifier for this commit
     * @return Unique commit identifier
     */
    public String getCommitId();

    /**
     * Retrieve the commit log message for the change.
     * @return Commit log
     */
    public String getCommitMessage();

    /**
     * Retrieves the Author of the commit. It could be useful to check who has
     * performed different updates to a set of resources
     * @return Committer
     */
    public Author getAuthor();

    /**
     * Retrieves the date-time stamp of when the commit was performed.
     * @return Date-time of the commit
     */
    public Date getCommitTime();

    /**
     * Get the parent commit id of the current commit.
     * @return Parent commit's id or blank string if root commit
     */
    public String getParentCommitId();
}
