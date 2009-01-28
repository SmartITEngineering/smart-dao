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
package com.smartitengineering.version.impl.jgit.service.impl;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.version.impl.jgit.domain.Commit;
import com.smartitengineering.version.impl.jgit.domain.Revision;
import com.smartitengineering.version.impl.jgit.service.MetaRCSService;

/**
 *
 * @author imyousuf
 */
public class MetaRCSServiceImpl
    implements MetaRCSService {

    protected CommonWriteDao<Commit> commitWriter;
    protected CommonReadDao<Commit> commitReader;
    protected CommonWriteDao<Revision> revisionWriter;
    protected CommonReadDao<Revision> revisionReader;

    /**
     * Get the value of revisionReader
     *
     * @return the value of revisionReader
     */
    public CommonReadDao<Revision> getRevisionReader() {
        return revisionReader;
    }

    /**
     * Set the value of revisionReader
     *
     * @param revisionReader new value of revisionReader
     */
    public void setRevisionReader(CommonReadDao<Revision> revisionReader) {
        this.revisionReader = revisionReader;
    }

    /**
     * Get the value of revisionWriter
     *
     * @return the value of revisionWriter
     */
    public CommonWriteDao<Revision> getRevisionWriter() {
        return revisionWriter;
    }

    /**
     * Set the value of revisionWriter
     *
     * @param revisionWriter new value of revisionWriter
     */
    public void setRevisionWriter(CommonWriteDao<Revision> revisionWriter) {
        this.revisionWriter = revisionWriter;
    }

    /**
     * Get the value of commitReader
     *
     * @return the value of commitReader
     */
    public CommonReadDao<Commit> getCommitReader() {
        return commitReader;
    }

    /**
     * Set the value of commitReader
     *
     * @param commitReader new value of commitReader
     */
    public void setCommitReader(CommonReadDao<Commit> commitReader) {
        this.commitReader = commitReader;
    }

    /**
     * Get the value of commitWriter
     *
     * @return the value of commitWriter
     */
    public CommonWriteDao<Commit> getCommitWriter() {
        return commitWriter;
    }

    /**
     * Set the value of commitWriter
     *
     * @param commitWriter new value of commitWriter
     */
    public void setCommitWriter(CommonWriteDao<Commit> commitWriter) {
        this.commitWriter = commitWriter;
    }

    public String[] getVersionsForResource(final String resourceId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getHeadVersionForResource(final String resourceId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveResources(
        final com.smartitengineering.version.api.Commit commit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteResources(
        final com.smartitengineering.version.api.Commit commit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
