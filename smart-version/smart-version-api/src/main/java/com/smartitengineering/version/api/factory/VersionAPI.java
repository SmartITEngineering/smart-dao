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
package com.smartitengineering.version.api.factory;

import com.smartitengineering.version.api.Author;
import com.smartitengineering.version.api.Commit;
import com.smartitengineering.version.api.Resource;
import com.smartitengineering.version.api.Revision;
import com.smartitengineering.version.api.VersionedResource;
import com.smartitengineering.version.api.impl.AuthorImpl;
import com.smartitengineering.version.api.impl.CommitImpl;
import com.smartitengineering.version.api.impl.ResourceImpl;
import com.smartitengineering.version.api.impl.RevisionImpl;
import com.smartitengineering.version.api.impl.VersionedResourceImpl;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 * Factory API for creating concrete class for Version's API interfaces
 * @author imyousuf
 */
public final class VersionAPI {

    private VersionAPI() {
    }

    /**
     * Creates an author to be used for setting to commits.
     * @param name Author's name, NULL string is not allowed
     * @param email Author's email, blank string is not allowed
     * @return Author to be used with commits
     */
    public static Author createAuthor(final String name,
                                      final String email) {
        if (StringUtils.isBlank(email) || name == null) {
            throw new IllegalArgumentException(
                "Name can't be NULL and email can't be blank!");
        }
        AuthorImpl authorImpl = new AuthorImpl();
        authorImpl.setEmail(email);
        authorImpl.setName(name);
        return authorImpl;
    }

    /**
     * Creates resource to versioned, it could be a new version or an old
     * resource's new version.
     * @param resourceId Id for the resource, not blank
     * @param content Content of the resource, not null
     * @return Returns the resource
     */
    public static Resource createResource(final String resourceId,
                                          final String content) {
        if (StringUtils.isBlank(resourceId) || content == null) {
            throw new IllegalArgumentException(
                "Content can't be NULL and resourceId can't be blank!");
        }
        ResourceImpl resourceImpl = new ResourceImpl();
        resourceImpl.setContent(content);
        resourceImpl.setId(resourceId);
        return resourceImpl;
    }

    /**
     * Create a revision object from the given resource and revision id.
     * @param resource Resource represented in the revision, can't be null
     * @param revisionId Id of the revision, might be null
     * @return Revision of the resource.
     */
    public static Revision createRevision(final Resource resource,
                                          final String revisionId) {
        if (resource == null) {
            throw new IllegalArgumentException("Resource can't be null!");
        }
        RevisionImpl revisionImpl = new RevisionImpl();
        revisionImpl.setResource(resource);
        revisionImpl.setRevisionId(revisionId);
        return revisionImpl;
    }

    /**
     * Create a versioned resource from revisions. If revisions is not empty
     * then head revision's resource will be pointed accordingly.
     * @param revisions Revisions of a resource
     * @return VersionedResource of the revisions.
     */
    public static VersionedResource getVersionedResource(
        Collection<Revision> revisions) {
        if (revisions == null) {
            revisions = Collections.emptyList();
        }
        VersionedResourceImpl versionedResourceImpl =
            new VersionedResourceImpl();
        if (!revisions.isEmpty()) {
            try {
                versionedResourceImpl.setHeadVersionResource(revisions.iterator().
                    next().getResource());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        versionedResourceImpl.setRevisions(revisions);
        return versionedResourceImpl;
    }

    /**
     * It creates a commit from the given information.
     * @param revisions Revisions to be part of the commit, can be empty
     * @param commitId Id of the commit, may be null
     * @param parentCommitId Id of the parent commit, may be null, the null
     *                       signifies that its the first commit of the base,
     *                       however, implementations could set it themselves if
     *                       required
     * @param commitMessage Log message for this commit, can not be blank
     * @param committer Author of the commit, can not be null
     * @param commitTime Time the commit was performed, may be null, can't be
     *                   null if commit id is not null or not blank.
     * @return Commit object representing the information supplied
     */
    public static Commit getCommit(final Collection<Revision> revisions,
                                   final String commitId,
                                   final String parentCommitId,
                                   final String commitMessage,
                                   final Author committer,
                                   final Date commitTime) {
        if (StringUtils.isBlank(commitMessage) || committer == null ||
            revisions == null || revisions.isEmpty() || (StringUtils.isNotBlank(
            commitId) && commitTime == null)) {
            throw new IllegalArgumentException(
                "Commit log message & revisions can't be blank and " +
                "committer can't be NULL, commitTime can't be null if commitId" +
                "is not blank!");
        }
        CommitImpl commitImpl = new CommitImpl();
        commitImpl.setAuthor(committer);
        commitImpl.setCommitId(commitId);
        commitImpl.setCommitMessage(commitMessage);
        commitImpl.setCommitTime(commitTime);
        commitImpl.setParentCommitId(parentCommitId);
        commitImpl.setRevisions(revisions);
        return commitImpl;
    }
}
