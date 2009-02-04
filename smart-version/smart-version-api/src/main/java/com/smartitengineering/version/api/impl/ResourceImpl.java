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
import com.smartitengineering.version.api.spi.MutableResource;
import org.apache.commons.lang.StringUtils;

/**
 * Default implementation of resource
 * @author imyousuf
 */
public class ResourceImpl
    implements Resource, MutableResource {

    private String id;
    private String content;
    private boolean deleted;
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
     * Get the value of content
     *
     * @return the value of content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the value of content
     *
     * @param content new value of content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Resource) {
            Resource resourceObj = (Resource) obj;
            if(StringUtils.isNotBlank(getId())) {
                return getId().equals(resourceObj.getId());
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if(StringUtils.isNotBlank(getId())) {
            return getId().hashCode();
        }
        return super.hashCode();
    }
}
