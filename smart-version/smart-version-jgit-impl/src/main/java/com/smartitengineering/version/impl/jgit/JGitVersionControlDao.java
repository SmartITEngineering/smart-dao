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
import com.smartitengineering.version.impl.jgit.service.MetaVCService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author imyousuf
 */
public class JGitVersionControlDao
    implements VersionControlDao {

    private VersionControlDao jGitImpl;
    private JGitDaoExtension jGitExtension;
    private MetaVCService jGitService;

    public void store(Commit commit,
                      final WriterCallback callback) {
        WriteStatus status = null;
        String comment = null;
        Throwable error = null;
        try {
            jGitImpl.store(commit, null);
            jGitService.saveResources(commit);
        }
        catch (Throwable ex) {
            status = WriteStatus.STORE_FAIL;
            comment = ex.getMessage();
            error = ex;
        }
        finally {
            if (callback != null) {
                callback.handle(commit, status, comment, error);
            }
        }
    }

    public void remove(Commit commit,
                       final WriterCallback callback) {
        WriteStatus status = null;
        String comment = null;
        Throwable error = null;
        try {
            jGitImpl.remove(commit, null);
            jGitService.deleteResources(commit);
        }
        catch (Throwable ex) {
            status = WriteStatus.STORE_FAIL;
            comment = ex.getMessage();
            error = ex;
        }
        finally {
            if (callback != null) {
                callback.handle(commit, status, comment, error);
            }
        }
    }

    public VersionedResource getVersionedResource(String resourceId) {
        String[] versions = jGitService.getVersionsForResource(resourceId);
        try {
            Map<String, byte[]> objectMap =
                jGitExtension.readBlobObjects(versions);
            Revision[] revisions = new Revision[objectMap.size()];
            int i = 0;
            for (String revisionId : versions) {
                String content = new String(objectMap.get(revisionId));
                revisions[i++] = VersionAPI.createRevision(VersionAPI.
                    createResource(resourceId, content), revisionId);
            }
            return VersionAPI.getVersionedResource(Arrays.asList(revisions));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Resource getResource(String resourceId) {
        try {
            return VersionAPI.createResource(resourceId,
                new String(jGitExtension.readObject(jGitService.
                getHeadVersionForResource(resourceId))));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Collection<Commit> searchForCommit(
        Collection<QueryParameter> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<VersionedResource> searchForVersionedResource(
        Collection<QueryParameter> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public MetaVCService getJGitService() {
        return jGitService;
    }

    public void setJGitService(MetaVCService jGitService) {
        this.jGitService = jGitService;
    }
}
