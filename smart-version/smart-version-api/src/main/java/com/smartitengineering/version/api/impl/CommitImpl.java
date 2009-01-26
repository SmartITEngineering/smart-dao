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

import com.smartitengineering.version.api.Author;
import com.smartitengineering.version.api.Commit;
import com.smartitengineering.version.api.Revision;
import com.smartitengineering.version.api.spi.MutableCommit;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 * Default implementation of commit
 * @author imyousuf
 */
public class CommitImpl
    implements Commit, MutableCommit {

    private Collection<Revision> revisions;
    private String commitId;
    private String commitMessage;
    private Author author;
    private String parentCommitId;
    protected Date commitTime;

    /**
     * Get the value of commitTime
     *
     * @return the value of commitTime
     */
    public Date getCommitTime() {
        return commitTime;
    }

    /**
     * Set the value of commitTime
     *
     * @param commitTime new value of commitTime
     */
    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    /**
     * Get the value of commitParentId
     *
     * @return the value of commitParentId
     */
    public String getParentCommitId() {
        return parentCommitId;
    }

    /**
     * Set the value of commitParentId
     *
     * @param commitParentId new value of commitParentId
     */
    public void setParentCommitId(String parentCommitId) {
        this.parentCommitId = parentCommitId;
    }

    /**
     * Get the value of author
     *
     * @return the value of author
     */
    public Author getAuthor() {
        if (author == null) {
            throw new IllegalStateException(
                "Author must be set before attempting to retrieve it!");
        }
        return author;
    }

    /**
     * Set the value of author
     *
     * @param author new value of author
     */
    public void setAuthor(Author author) {
        if (author == null) {
            return;
        }
        this.author = author;
    }

    /**
     * Get the value of commitMessage
     *
     * @return the value of commitMessage
     */
    public String getCommitMessage() {
        if (StringUtils.isBlank(commitMessage)) {
            throw new IllegalStateException(
                "Commit message (unblank) must be set before attempting to retrieve it!");
        }
        return commitMessage;
    }

    /**
     * Set the value of commitMessage
     *
     * @param commitMessage new value of commitMessage
     */
    public void setCommitMessage(String commitMessage) {
        if(StringUtils.isBlank(commitMessage)) {
            return ;
        }
        this.commitMessage = commitMessage;
    }

    /**
     * Get the value of commitId
     *
     * @return the value of commitId
     */
    public String getCommitId() {
        return commitId;
    }

    /**
     * Set the value of commitId
     *
     * @param commitId new value of commitId
     */
    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    /**
     * Get the value of revisions
     *
     * @return the value of revisions
     */
    public Collection<Revision> getRevisions() {
        if(revisions == null) {
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
}
