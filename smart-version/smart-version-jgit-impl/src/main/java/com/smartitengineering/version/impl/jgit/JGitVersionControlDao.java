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

import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.version.api.Commit;
import com.smartitengineering.version.api.Resource;
import com.smartitengineering.version.api.Revision;
import com.smartitengineering.version.api.VersionedResource;
import com.smartitengineering.version.api.dao.VersionControlDao;
import com.smartitengineering.version.api.dao.WriteStatus;
import com.smartitengineering.version.api.dao.WriterCallback;
import com.smartitengineering.version.api.factory.VersionAPI;
import com.smartitengineering.version.impl.jgit.service.MetaRCSService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class JGitVersionControlDao
    implements VersionControlDao {

    private VersionControlDao jGitImpl;
    private JGitDaoExtension jGitExtension;
    private MetaRCSService jGitService;

    public void store(Commit commit,
                      final WriterCallback callback) {
        final Status myStatus = new Status();
        try {
            jGitImpl.store(commit, new WriterCallback() {

                public void handle(Commit commit,
                                   WriteStatus status,
                                   String comment,
                                   Throwable error) {
                    myStatus.myStatus = status;
                    myStatus.myComment = comment;
                    myStatus.myError = error;
                    if (status == WriteStatus.STORE_PASS) {
                        try {
                            jGitService.saveResources(commit);
                        }
                        catch (Throwable ex) {
                            myStatus.myStatus = WriteStatus.STORE_FAIL;
                            myStatus.myComment = ex.getMessage();
                            myStatus.myError = ex;
                        }
                        finally {
                            if (callback != null) {
                                callback.handle(commit, myStatus.myStatus,
                                    myStatus.myComment,
                                    myStatus.myError);
                            }
                        }
                    }
                }
            });
        }
        catch (Throwable ex) {
            ex.printStackTrace();
            myStatus.myStatus = WriteStatus.STORE_FAIL;
            myStatus.myComment = ex.getMessage();
            myStatus.myError = ex;
            if (callback != null) {
                callback.handle(commit, myStatus.myStatus, myStatus.myComment,
                    myStatus.myError);
            }
        }
    }

    private class Status {

        public WriteStatus myStatus;
        public String myComment;
        public Throwable myError;
    }

    public void remove(Commit commit,
                       final WriterCallback callback) {
        final Status myStatus = new Status();
        try {
            jGitImpl.remove(commit, new WriterCallback() {

                public void handle(Commit commit,
                                   WriteStatus status,
                                   String comment,
                                   Throwable error) {
                    myStatus.myStatus = status;
                    myStatus.myComment = comment;
                    myStatus.myError = error;
                    if (status == WriteStatus.STORE_PASS) {
                        try {
                            jGitService.deleteResources(commit);
                        }
                        catch (Throwable ex) {
                            myStatus.myStatus = WriteStatus.STORE_FAIL;
                            myStatus.myComment = ex.getMessage();
                            myStatus.myError = ex;
                        }
                        finally {
                            if (callback != null) {
                                callback.handle(commit, myStatus.myStatus,
                                    myStatus.myComment,
                                    myStatus.myError);
                            }
                        }
                    }
                }
            });
        }
        catch (Throwable ex) {
            ex.printStackTrace();
            myStatus.myStatus = WriteStatus.STORE_FAIL;
            myStatus.myComment = ex.getMessage();
            myStatus.myError = ex;
            if (callback != null) {
                callback.handle(commit, myStatus.myStatus, myStatus.myComment,
                    myStatus.myError);
            }
        }
    }

    public VersionedResource getVersionedResource(String resourceId) {
        String trimmedResourceId = VersionAPI.trimToPropertResourceId(
            resourceId);
        if (StringUtils.isBlank(trimmedResourceId)) {
            throw new IllegalArgumentException("Invalid resource id!");
        }
        String[] versions = jGitService.getVersionsForResource(trimmedResourceId);
        try {
            Map<String, byte[]> objectMap =
                jGitExtension.readBlobObjects(versions);
            Revision[] revisions = new Revision[objectMap.size()];
            int i = 0;
            for (String revisionId : versions) {
                String content = new String(objectMap.get(revisionId));
                revisions[i++] = VersionAPI.createRevision(VersionAPI.
                    createResource(trimmedResourceId, content), revisionId);
            }
            return VersionAPI.createVersionedResource(Arrays.asList(revisions));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public Resource getResource(String resourceId) {
        try {
            String trimmedResourceId = VersionAPI.trimToPropertResourceId(
                resourceId);
            if (StringUtils.isBlank(trimmedResourceId)) {
                throw new IllegalArgumentException("Invalid resource id!");
            }
            final String headVersionForResource =
                jGitService.getHeadVersionForResource(trimmedResourceId);
            final byte[] objectBytes =
                jGitExtension.readObject(headVersionForResource);
            if(objectBytes == null || objectBytes.length <= 0) {
                throw new IllegalArgumentException("Resource doesn't exist!");
            }
            return VersionAPI.createResource(trimmedResourceId,
                new String(objectBytes));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public Resource getResourceByRevision(String revisionId,
                                          String resourceId) {
        try {
            String trimmedResourceId = VersionAPI.trimToPropertResourceId(
                resourceId);
            if (StringUtils.isBlank(trimmedResourceId)) {
                throw new IllegalArgumentException("Invalid resource id!");
            }
            return VersionAPI.createResource(trimmedResourceId,
                new String(jGitExtension.readObject(revisionId)));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * Only use property param from SearchProperty that start with 'COMMIT'.
     * @see VersionControlDao#searchForCommits(java.util.Collection) 
     * @see SearchParam#getPropertyName() 
     * @see SearchProperties
     */
    public Collection<Commit> searchForCommits(
        Collection<QueryParameter> parameters) {
        Set<Commit> commits = jGitService.searchForCommits(parameters);
        return commits;
    }

    /**
     * Only use property param from SearchProperty that start with 'REVISION'.
     * @see VersionControlDao#searchForRevisions(java.util.Collection)
     * @see SearchParam#getPropertyName() 
     * @see SearchProperties
     */
    public Collection<Revision> searchForRevisions(
        Collection<QueryParameter> parameters) {
        Set<Revision> revisions = jGitService.searchForRevisions(parameters);
        return revisions;
    }

    public JGitDaoExtension getJGitExtension() {
        return jGitExtension;
    }

    public void setJGitExtension(JGitDaoExtension jGitExtension) {
        this.jGitExtension = jGitExtension;
    }

    public VersionControlDao getJGitImpl() {
        return jGitImpl;
    }

    public void setJGitImpl(VersionControlDao jGitImpl) {
        this.jGitImpl = jGitImpl;
    }

    public MetaRCSService getJGitService() {
        return jGitService;
    }

    public void setJGitService(MetaRCSService jGitService) {
        this.jGitService = jGitService;
    }

}
