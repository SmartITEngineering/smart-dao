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
package com.smartitengineering.version.impl.jgit.service;

import com.smartitengineering.util.spring.BeanFactoryRegistrar;
import com.smartitengineering.util.spring.annotations.Aggregator;
import com.smartitengineering.util.spring.annotations.InjectableField;
import com.smartitengineering.version.api.factory.VersionAPI;
import com.smartitengineering.version.impl.jgit.domain.Author;
import com.smartitengineering.version.impl.jgit.domain.Commit;
import com.smartitengineering.version.impl.jgit.domain.Resource;
import com.smartitengineering.version.impl.jgit.domain.Revision;
import java.util.LinkedHashSet;

/**
 *
 * @author imyousuf
 */
@Aggregator(contextName = "com.smartitnengineering.smart-dao.smart-version-jgit")
public class MetaFactory {

    private static MetaFactory factory;
    @InjectableField(beanName = "metaRCSConfig")
    private RCSConfig config;
    @InjectableField
    private MetaRCSService metaRCSService;

    public RCSConfig getConfig() {
        if (config == null) {
            config = new RCSConfig();
            config.setAllowNoChangeCommit(false);
            config.setConcurrentWriteOperations(1);
        }
        return config;
    }

    public MetaRCSService getMetaRCSService() {
        return metaRCSService;
    }

    private MetaFactory() {
        BeanFactoryRegistrar.aggregate(this);
    }

    public static final MetaFactory getInstance() {
        if (factory == null) {
            factory = new MetaFactory();
        }
        return factory;
    }

    public static Commit transformAPICommit(
        final com.smartitengineering.version.api.Commit apiCommit,
        final boolean deleted) {
        Commit commit = new Commit();
        commit.setCommitId(apiCommit.getCommitId());
        commit.setCommitMessage(apiCommit.getCommitMessage());
        Author committer = new Author();
        committer.setName(apiCommit.getAuthor().getName());
        committer.setEmail(apiCommit.getAuthor().getEmail());
        commit.setCommitter(committer);
        commit.setDateTime(apiCommit.getCommitTime());
        commit.setParentCommitId(apiCommit.getParentCommitId());
        for (com.smartitengineering.version.api.Revision apiRevision : apiCommit.
            getRevisions()) {
            commit.getRevisions().add(transformAPIRevision(commit, apiRevision,
                deleted));
        }
        return commit;
    }

    public static Revision transformAPIRevision(
        final Commit commit,
        final com.smartitengineering.version.api.Revision apiRevision,
        final boolean deleted) {
        Revision revision = new Revision();
        revision.setCommit(commit);
        revision.setDeleted(deleted);
        Resource resource = new Resource();
        resource.setResourceId(apiRevision.getResource().getId());
        revision.setResource(resource);
        revision.setRevisionId(apiRevision.getRevisionId());
        return revision;
    }

    public static com.smartitengineering.version.api.Commit transformMetaCommit(
        Commit commit) {
        com.smartitengineering.version.api.Author apiAuthor = VersionAPI.
            createAuthor(commit.getCommitter().getName(), commit.getCommitter().
            getEmail());
        LinkedHashSet<com.smartitengineering.version.api.Revision> apiRevisions =
            new LinkedHashSet<com.smartitengineering.version.api.Revision>(commit.getRevisions().
            size());
        for (Revision revision : commit.getRevisions()) {
            apiRevisions.add(transformMetaRevision(revision));
        }
        return VersionAPI.createCommit(apiRevisions, commit.getCommitId(),
            commit.getParentCommitId(), commit.getCommitMessage(), apiAuthor,
            commit.getDateTime());
    }

    public static com.smartitengineering.version.api.Revision transformMetaRevision(
        Revision revision) {
        final com.smartitengineering.version.api.Resource apiResource;
        if (VersionAPI.getInstance().getVersionControlDao() == null) {
            apiResource = VersionAPI.createResource(revision.getResource().
                getResourceId(), "");
        }
        else {
            apiResource =
                VersionAPI.getInstance().getVersionControlDao().
                getResourceByRevision(
                revision.getRevisionId(), revision.getResource().getResourceId());
        }
        return VersionAPI.createRevision(apiResource, revision.getRevisionId());
    }
}
