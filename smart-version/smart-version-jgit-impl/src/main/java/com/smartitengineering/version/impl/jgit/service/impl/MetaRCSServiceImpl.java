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
import com.smartitengineering.dao.common.queryparam.MatchMode;
import com.smartitengineering.dao.common.queryparam.Order;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.version.impl.jgit.SearchProperties;
import com.smartitengineering.version.impl.jgit.domain.Commit;
import com.smartitengineering.version.impl.jgit.domain.Resource;
import com.smartitengineering.version.impl.jgit.domain.Revision;
import com.smartitengineering.version.impl.jgit.service.MetaFactory;
import com.smartitengineering.version.impl.jgit.service.MetaRCSService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

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
        if (StringUtils.isBlank(resourceId)) {
            throw new IllegalArgumentException("Blank resource not supported!");
        }
        final List<String> revisions =
            (List<String>) revisionReader.getOtherList(
            QueryParameterFactory.<String>getStringLikePropertyParam(
            new StringBuilder(Revision.PROP_RESOURCE).append('.').
            append(Resource.PROP_RESOURCEID).toString(),
            resourceId, MatchMode.EXACT),
            QueryParameterFactory.getPropProjectionParam(
            Revision.PROP_REVISIONID),
            QueryParameterFactory.<Boolean>getEqualPropertyParam(
            SearchProperties.REVISION_RESOURCE_DELETED.getPropertyName(), false),
            QueryParameterFactory.getOrderByParam("id", Order.DESC));
        return revisions.toArray(new String[0]);
    }

    public String getHeadVersionForResource(final String resourceId) {
        if (StringUtils.isBlank(resourceId)) {
            throw new IllegalArgumentException("Blank resource not supported!");
        }
        final String revision =
            (String) revisionReader.getOther(QueryParameterFactory.<String>
            getStringLikePropertyParam(new StringBuilder(
            Revision.PROP_RESOURCE).append('.').
            append(Resource.PROP_RESOURCEID).toString(),
            resourceId, MatchMode.EXACT),
            QueryParameterFactory.<Boolean>getEqualPropertyParam(
            Revision.PROP_HEADREVISION, true),
            QueryParameterFactory.getPropProjectionParam(
            Revision.PROP_REVISIONID),
            QueryParameterFactory.<Boolean>getEqualPropertyParam(
            SearchProperties.REVISION_RESOURCE_DELETED.getPropertyName(), false));
        return revision;
    }

    public void saveResources(
        final com.smartitengineering.version.api.Commit commit) {
        Commit commitDomain = MetaFactory.transformAPICommit(commit);
        persistCommit(commitDomain);
    }

    private void persistCommit(Commit commitDomain)
        throws IllegalStateException {
        //Set all revisions in commit to 'head' and update all the revisions for
        //the resources already in the system to 'not head'
        if (!commitDomain.isValid()) {
            throw new IllegalStateException(
                "Commit domain's current state is not valid!");
        }
        Set<String> resourceIds = new HashSet<String>();
        Set<Revision> currentHeads = new HashSet<Revision>();
        for (Revision revision : commitDomain.getRevisions()) {
            revision.setHeadRevision(true);
            String resourceId = revision.getResource().getResourceId();
            resourceIds.add(resourceId);
        }
        currentHeads = new HashSet<Revision>(revisionReader.getList(
            QueryParameterFactory.<String>getIsInPropertyParam(new StringBuilder(Revision.PROP_RESOURCE).append('.').
            append(Resource.PROP_RESOURCEID).toString(),
            resourceIds.toArray(new String[0])),
            QueryParameterFactory.<Boolean>getEqualPropertyParam(
            Revision.PROP_HEADREVISION, true)));
        commitWriter.save(commitDomain);
        if (currentHeads != null && !currentHeads.isEmpty()) {
            for (Revision revision : currentHeads) {
                revision.setHeadRevision(false);
            }
            revisionWriter.update(currentHeads.toArray(new Revision[0]));
        }
    }

    public Set<com.smartitengineering.version.api.Commit> searchForCommits(
        Collection<QueryParameter> parameters) {
        Collection<QueryParameter> params = parameters == null ? Collections.<QueryParameter>emptyList()
            : parameters;
        List<Commit> commits = commitReader.getList(params.toArray(
            new QueryParameter[0]));
        LinkedHashSet<com.smartitengineering.version.api.Commit> result =
            new LinkedHashSet<com.smartitengineering.version.api.Commit>();
        if (commits != null) {
            for (Commit commit : commits) {
                result.add(MetaFactory.transformMetaCommit(commit));
            }
        }
        return result;
    }

    public Set<com.smartitengineering.version.api.Revision> searchForRevisions(
        Collection<QueryParameter> parameters) {
        Collection<QueryParameter> params = parameters == null ? Collections.<QueryParameter>emptyList()
            : parameters;
        List<Revision> revisions = revisionReader.getList(params.toArray(
            new QueryParameter[0]));
        LinkedHashSet<com.smartitengineering.version.api.Revision> result =
            new LinkedHashSet<com.smartitengineering.version.api.Revision>();
        if (revisions != null) {
            for (Revision revision : revisions) {
                result.add(MetaFactory.transformMetaRevision(revision));
            }
        }
        return result;
    }
}
