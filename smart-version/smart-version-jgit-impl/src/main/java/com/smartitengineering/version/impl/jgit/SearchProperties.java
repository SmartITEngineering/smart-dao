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

import com.smartitengineering.version.impl.jgit.domain.Author;
import com.smartitengineering.version.impl.jgit.domain.Commit;
import com.smartitengineering.version.impl.jgit.domain.Resource;
import com.smartitengineering.version.impl.jgit.domain.Revision;

/**
 * Represents enum for property name of a query parameter to be used for
 * querying to the Version API. While using it please pay attention to the types
 * of each property and use them wisely; we would like to mention that we will
 * pass through your query params directly without verifying them.
 * @author imyousuf
 */
public enum SearchProperties
    implements SearchParam {

    /**
     * The java.util.Date of a commit.
     */
    COMMIT_DATE(Commit.PROP_DATETIME),
    /**
     * The string log message of a commit
     */
    COMMIT_MSG(Commit.PROP_COMMITMESSAGE),
    /**
     * Use revisions as a nested parameters to filter commits by revisions props
     * available as in 'REVISION*'
     */
    COMMIT_REVISIONS(Commit.PROP_REVISIONS),
    /**
     * The full name of an committer, a string.
     */
    COMMITTER_NAME(Commit.PROP_COMMITTER + '.' + Author.PROP_NAME),
    /**
     * The email address of a committer, a string.
     */
    COMMITTER_EMAIL(Commit.PROP_COMMITTER + '.' + Author.PROP_EMAIL),
    /**
     * Specify the interested resource id and it accepts a String.
     */
    REVISION_RESOURCE(Revision.PROP_RESOURCE + '.' + Resource.PROP_RESOURCEID),
    /**
     * Use this BOOLEAN field to specify to search within HEAD revisions only or
     * not
     */
    REVISION_HEAD(Revision.PROP_HEADREVISION),
    /**
     * Filter whether the revision's resource is deleted or not, its a boolean.
     */
    REVISION_RESOURCE_DELETED(Revision.PROP_RESOURCE + '.' + Resource.PROP_DELETE),
    /**
     * <p>Use this to search for properties of a commit of a revision</p>
     * <p>While others can be used as property directly, in order to use this we
     * will need to create a nested property param and then use COMMIT* props
     * perform search.</p>
     */
    REVISION_COMMIT(Revision.PROP_COMMIT);
    private final String propertyName;

    private SearchProperties(final String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
