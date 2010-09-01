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
public class Resource
    extends AbstractPersistentDTO<Resource> {

    public static final String PROP_RESOURCEID = "resourceId";
    public static final String PROP_DELETE = "deleted";
    public static final String PROP_MIMETYPE = "mimeType";
    protected String resourceId;
    protected boolean deleted;
    protected String mimeType;

    /**
     * Get the value of mimeType
     *
     * @return the value of mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Set the value of mimeType
     *
     * @param mimeType new value of mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Get the value of deleted
     *
     * @return the value of deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Set the value of deleted
     *
     * @param deleted new value of deleted
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Get the value of resourceId
     *
     * @return the value of resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Set the value of resourceId
     *
     * @param resourceId new value of resourceId
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public boolean isValid() {
        if (StringUtils.isBlank(resourceId)) {
            return false;
        }
        return true;
    }
}
