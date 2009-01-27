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
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.version.impl.jgit.service.MetaFactory;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class Commit
    extends AbstractPersistentDTO<Commit>
    implements PersistentDTO<Commit> {

    public static final String PROP_REVISIONS = "revisions";
    public static final String PROP_COMMITTER = "committer";
    public static final String PROP_DATETIME = "dateTime";
    public static final String PROP_COMMITID = "commitId";
    public static final String PROP_COMMITMESSAGE = "commitMessage";
    public static final String PROP_PARENTCOMMITID = "parentCommitId";
    protected Author committer;
    protected Set<Revision> revisions;
    protected Date dateTime;
    protected String commitId;
    protected String commitMessage;
    protected String parentCommitId;

    /**
     * Get the value of parentCommitId
     *
     * @return the value of parentCommitId
     */
    public String getParentCommitId() {
        return parentCommitId;
    }

    /**
     * Set the value of parentCommitId
     *
     * @param parentCommitId new value of parentCommitId
     */
    public void setParentCommitId(String parentCommitId) {
        this.parentCommitId = parentCommitId;
    }

    /**
     * Get the value of commitMessage
     *
     * @return the value of commitMessage
     */
    public String getCommitMessage() {
        return commitMessage;
    }

    /**
     * Set the value of commitMessage
     *
     * @param commitMessage new value of commitMessage
     */
    public void setCommitMessage(String commitMessage) {
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
     * Get the value of dateTime
     *
     * @return the value of dateTime
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Set the value of dateTime
     *
     * @param dateTime new value of dateTime
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Get the value of revisions
     *
     * @return the value of revisions
     */
    public Set<Revision> getRevisions() {
        if (revisions == null) {
            revisions = new LinkedHashSet<Revision>();
        }
        return revisions;
    }

    /**
     * Set the value of revisions
     *
     * @param revisions new value of revisions
     */
    public void setRevisions(Set<Revision> revisions) {
        this.revisions = revisions;
    }

    /**
     * Get the value of committer
     *
     * @return the value of committer
     */
    public Author getCommitter() {
        return committer;
    }

    /**
     * Set the value of committer
     *
     * @param committer new value of committer
     */
    public void setCommitter(Author committer) {
        this.committer = committer;
    }

    public boolean isValid() {
        if (committer == null || !committer.isValid()) {
            return false;
        }
        if (StringUtils.isBlank(commitMessage)) {
            return false;
        }
        if (StringUtils.isBlank(commitId)) {
            return false;
        }
        if (dateTime == null) {
            return false;
        }
        if (!MetaFactory.getInstance().getConfig().isAllowNoChangeCommit() && getRevisions().
            isEmpty()) {
            return false;
        }
        for(Revision revision : getRevisions()) {
            if(!revision.isValid()) {
                return false;
            }
        }
        return true;
    }
}
