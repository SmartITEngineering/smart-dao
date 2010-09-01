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
package com.smartitengineering.version.impl.jgit.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class Revision
    extends AbstractPersistentDTO<Revision> {

    public static final String PROP_RESOURCE = "resource";
    public static final String PROP_REVISIONID = "revisionId";
    public static final String PROP_HEADREVISION = "headRevision";
    public static final String PROP_COMMIT = "commit";
    protected Resource resource;
    protected String revisionId;
    protected boolean headRevision;
    protected Commit commit;

    /**
     * Get the value of commit
     *
     * @return the value of commit
     */
    public Commit getCommit() {
        return commit;
    }

    /**
     * Set the value of commit
     *
     * @param commit new value of commit
     */
    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    /**
     * Get the value of headRevision
     *
     * @return the value of headRevision
     */
    public boolean isHeadRevision() {
        return headRevision;
    }

    /**
     * Set the value of headRevision
     *
     * @param headRevision new value of headRevision
     */
    public void setHeadRevision(boolean headRevision) {
        this.headRevision = headRevision;
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

    /**
     * Get the value of resource
     *
     * @return the value of resource
     */
    public Resource getResource() {
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

    public boolean isValid() {
        if (StringUtils.isBlank(revisionId) || resource == null || !resource.
            isValid()) {
            return false;
        }
        if(commit == null) {
            return false;
        }
        return true;
    }
}
