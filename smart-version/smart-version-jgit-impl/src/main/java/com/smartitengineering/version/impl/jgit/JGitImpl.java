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
import com.smartitengineering.version.api.dao.VersionControlReadDao;
import com.smartitengineering.version.api.dao.VersionControlWriteDao;
import com.smartitengineering.version.api.dao.WriteStatus;
import com.smartitengineering.version.api.dao.WriterCallback;
import com.smartitengineering.version.api.factory.VersionAPI;
import com.smartitengineering.version.api.spi.MutableCommit;
import com.smartitengineering.version.api.spi.MutableRevision;
import com.smartitengineering.version.impl.jgit.service.MetaFactory;
import com.smartitengineering.version.impl.jgit.service.RCSConfig;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileTreeEntry;
import org.eclipse.jgit.lib.GitIndex;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectWriter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.Tree;
import org.eclipse.jgit.lib.TreeEntry;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

/**
 *
 * @author imyousuf
 */
public class JGitImpl
        implements VersionControlWriteDao,
        VersionControlReadDao,
        JGitDaoExtension {

    private String readRepositoryLocation;
    private String writeRepositoryLocation;
    private Repository writeRepository;
    private ObjectWriter objectWriter;
    private Repository readRepository;
    private ExecutorService executorService;
    private RCSConfig config;
    private boolean initialized;

    public JGitImpl() {
    }

    public void init()
            throws IOException {
        if (initialized) {
            throw new IllegalStateException("Impl already initialized");
        }
        if (StringUtils.isBlank(getReadRepositoryLocation()) || StringUtils.isBlank(getWriteRepositoryLocation())) {
            throw new IllegalStateException("Repository location not set!");
        }
        File writeRepoDir = new File(getWriteRepositoryLocation());
        writeRepository = new Repository(writeRepoDir);
        if (!writeRepoDir.exists()) {
            writeRepository.create();
        }
        writeRepository.close();
        File readRepoDir = new File(getReadRepositoryLocation());
        readRepository = new Repository(readRepoDir);
        if (!readRepoDir.exists()) {
            readRepository.create();
        }
        readRepository.close();
        executorService = Executors.newFixedThreadPool(getConfig().
                getConcurrentWriteOperations());
        initialized = true;
    }

    public void finish() {
        if (writeRepository != null) {
            writeRepository.close();
        }
        if (readRepository != null) {
            readRepository.close();
        }
    }

    protected void checkInitialized() {
        if (!initialized) {
            throw new IllegalArgumentException("After constructing please "
                    + "set repository location and then invoke init() before "
                    + "attempting to use any other operations");
        }
    }

    public RCSConfig getConfig() {
        if (config == null) {
            return MetaFactory.getInstance().getConfig();
        }
        return config;
    }

    public void setConfig(RCSConfig config) {
        this.config = config;
    }

    public void setRepositoryLocation(final String repositoryLocation) {
        if (StringUtils.isBlank(repositoryLocation)) {
            throw new IllegalArgumentException("Repo location can't be blank!");
        }
        this.readRepositoryLocation = repositoryLocation;
        this.writeRepositoryLocation = repositoryLocation;
    }

    public String getReadRepositoryLocation() {
        if (StringUtils.isBlank(readRepositoryLocation)) {
            return getConfig().getRepositoryReadPath();
        }
        return readRepositoryLocation;
    }

    public void setReadRepositoryLocation(final String readRepositoryLocation) {
        if (StringUtils.isBlank(readRepositoryLocation)) {
            throw new IllegalArgumentException("Repo location can't be blank!");
        }
        this.readRepositoryLocation = readRepositoryLocation;
    }

    public String getWriteRepositoryLocation() {
        if (StringUtils.isBlank(writeRepositoryLocation)) {
            return getConfig().getRepositoryWritePath();
        }
        return writeRepositoryLocation;
    }

    public void setWriteRepositoryLocation(final String writeRepositoryLocation) {
        if (StringUtils.isBlank(writeRepositoryLocation)) {
            throw new IllegalArgumentException("Repo location can't be blank!");
        }
        this.writeRepositoryLocation = writeRepositoryLocation;
    }

    public synchronized Repository getReadRepository() {
        return readRepository;
    }

    protected synchronized void reInitReadRepository()
            throws IOException {
        readRepository.close();
        readRepository = new Repository(new File(getReadRepositoryLocation()));
    }

    public Repository getWriteRepository() {
        return writeRepository;
    }

    public void store(final Commit commit,
            final WriterCallback callback) {
        executorService.submit(new Runnable() {

            public void run() {
                WriteStatus status = null;
                String comment = null;
                Throwable error = null;
                try {
                    Tree head = getHeadTree(writeRepository);
                    addOrUpdateToHead(commit, head);
                    prepareCommit(head, commit);
                    status = WriteStatus.STORE_PASS;
                    comment = "OK";
                    error = null;
                } catch (Throwable ex) {
                    status = WriteStatus.STORE_FAIL;
                    comment = ex.getMessage();
                    error = ex;
                    throw new RuntimeException(ex);
                } finally {
                    if (callback != null) {
                        callback.handle(commit, status, comment, error);
                    }
                    try {
                        reInitReadRepository();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public VersionedResource getVersionedResource(final String resourceId) {
        try {
            String trimmedResourceId = VersionAPI.trimToProperResourceId(
                    resourceId);
            if (StringUtils.isBlank(trimmedResourceId)) {
                throw new IllegalArgumentException("Invalid resource id!");
            }
            Set<ObjectId> revisionIds = getGraphForResourceId(trimmedResourceId);
            if (revisionIds == null || revisionIds.isEmpty()) {
                throw new IllegalArgumentException("Resource id doesn't exist!");
            }
            Revision[] revisions = new Revision[revisionIds.size()];
            int i = 0;
            for (ObjectId revisionId : revisionIds) {
                String revisionIdStr = ObjectId.toString(revisionId);
                String content = new String(readObject(revisionIdStr));
                revisions[i++] = VersionAPI.createRevision(VersionAPI.createResource(trimmedResourceId, content), revisionIdStr);
            }
            return VersionAPI.createVersionedResource(Arrays.asList(revisions));
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public Resource getResource(final String resourceId) {
        try {
            String trimmedResourceId = VersionAPI.trimToProperResourceId(
                    resourceId);
            if (StringUtils.isBlank(trimmedResourceId)) {
                throw new IllegalArgumentException("Invalid resource id!");
            }
            ObjectId resourceObjectId;
            Tree head = getHeadTree(getReadRepository());
            if (!head.existsBlob(trimmedResourceId)) {
                throw new IllegalArgumentException("Resource id doesn't exist!");
            }
            TreeEntry treeEntry = head.findBlobMember(trimmedResourceId);
            resourceObjectId = treeEntry.getId();
            return VersionAPI.createResource(trimmedResourceId, new String(
                    readObject(ObjectId.toString(resourceObjectId))));
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public Resource getResourceByRevision(final String revisionId,
            final String resourceId) {
        try {
            String trimmedResourceId = VersionAPI.trimToProperResourceId(
                    resourceId);
            if (StringUtils.isBlank(trimmedResourceId)) {
                throw new IllegalArgumentException("Invalid resource id!");
            }
            ObjectId resourceObjectId;
            resourceObjectId = ObjectId.fromString(revisionId);
            return VersionAPI.createResource(trimmedResourceId, new String(
                    readObject(ObjectId.toString(resourceObjectId))));
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] readObject(final String objectIdStr)
            throws IOException,
            IllegalArgumentException {
        if (StringUtils.isBlank(objectIdStr)) {
            throw new IllegalArgumentException("Invalid Object id!");
        }
        ObjectId objectId = ObjectId.fromString(objectIdStr);
        ObjectLoader objectLoader = getReadRepository().openObject(objectId);
        if (objectLoader.getType() != Constants.OBJ_BLOB) {
            throw new IllegalArgumentException("Not a blob: " + objectIdStr);
        }
        return objectLoader.getBytes();
    }

    public Collection<Commit> searchForCommits(
            final Collection<QueryParameter> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Revision> searchForRevisions(
            final Collection<QueryParameter> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, byte[]> readBlobObjects(final String... objectIds)
            throws IOException,
            IllegalArgumentException {
        checkInitialized();
        if (objectIds == null || objectIds.length <= 0) {
            throw new IllegalArgumentException("Empty Object IDs!");
        }
        Map<String, byte[]> blobs =
                new HashMap<String, byte[]>(objectIds.length);
        for (String objectIdStr : objectIds) {
            byte[] bytes = readObject(objectIdStr);
            blobs.put(objectIdStr, bytes);
        }
        return blobs;
    }

    protected Tree getHeadTree(Repository repository)
            throws IOException {
        Tree head;
        org.eclipse.jgit.lib.Commit headCommit = repository.mapCommit(
                Constants.HEAD);
        if (headCommit == null) {
            head = new Tree(repository);
        } else {
            head = headCommit.getTree();
        }
        return head;
    }

    protected ObjectId addOrUpdateToHead(final Commit commit,
            final Tree head)
            throws IOException {
        for (Revision revision : commit.getRevisions()) {
            String objectPath = revision.getResource().getId();
            FileTreeEntry treeEntry;
            boolean newEntry = false;
            if (head.existsBlob(objectPath)) {
                treeEntry = (FileTreeEntry) head.findBlobMember(objectPath);
            } else {
                treeEntry = head.addFile(objectPath);
                newEntry = true;
            }
            treeEntry.setExecutable(false);
            if (!revision.getResource().isDeleted()) {
                if (revision instanceof MutableRevision) {
                    ObjectId revisionId = getObjectWriter().writeBlob(
                            revision.getResource().getContentSize(),
                            revision.getResource().getContentAsStream());
                    MutableRevision mutableRevision = (MutableRevision) revision;
                    mutableRevision.setRevisionId(ObjectId.toString(revisionId));
                    treeEntry.setId(revisionId);
                } else {
                    throw new IllegalArgumentException(
                            "SPI not implemented by API!");
                }
            } else if (!newEntry) {
                if (revision instanceof MutableRevision) {
                    ObjectId revisionId = treeEntry.getId();
                    MutableRevision mutableRevision = (MutableRevision) revision;
                    mutableRevision.setRevisionId(ObjectId.toString(revisionId));
                    treeEntry.delete();
                } else {
                    throw new IllegalArgumentException(
                            "SPI not implemented by API!");
                }
            }
        }
        GitIndex index = getWriteRepository().getIndex();
        index.readTree(head);
        index.write();
        ObjectId newHeadId = index.writeTree();
        head.setId(newHeadId);
        return newHeadId;
    }

    protected ObjectWriter getObjectWriter() {
        if (objectWriter == null) {
            objectWriter = new ObjectWriter(getWriteRepository());
        }
        return objectWriter;
    }

    protected void performCommit(final Commit newCommit,
            final Tree head)
            throws IOException {
        ObjectId[] parentIds;
        ObjectId currentHeadId = getWriteRepository().resolve(Constants.HEAD);
        if (currentHeadId != null) {
            parentIds = new ObjectId[]{currentHeadId};
        } else {
            parentIds = new ObjectId[0];
        }
        org.eclipse.jgit.lib.Commit commit = new org.eclipse.jgit.lib.Commit(
                getWriteRepository(), parentIds);
        commit.setTree(head);
        commit.setTreeId(head.getId());
        PersonIdent person = new PersonIdent(newCommit.getAuthor().getName(),
                newCommit.getAuthor().getEmail());
        commit.setAuthor(person);
        commit.setCommitter(person);
        commit.setMessage(newCommit.getCommitMessage());
        ObjectId newCommitId = getObjectWriter().writeCommit(commit);
        if (newCommit instanceof MutableCommit) {
            MutableCommit mutableCommit = (MutableCommit) newCommit;
            mutableCommit.setCommitId(ObjectId.toString(newCommitId));
            mutableCommit.setCommitTime(commit.getCommitter().getWhen());
            commit.setCommitId(newCommitId);
            if (commit.getParentIds().length > 0) {
                mutableCommit.setParentCommitId(ObjectId.toString(commit.getParentIds()[0]));
            } else {
                mutableCommit.setParentCommitId(ObjectId.toString(ObjectId.zeroId()));
            }
        } else {
            throw new IllegalArgumentException("SPI not implemented by API!");
        }
        RefUpdate refUpdate =
                getWriteRepository().updateRef(Constants.HEAD);
        refUpdate.setNewObjectId(commit.getCommitId());
        refUpdate.setRefLogMessage(commit.getMessage(), false);
        refUpdate.forceUpdate();
    }

    protected void prepareCommit(final Tree head,
            final Commit commit)
            throws IOException,
            IOException {
        ObjectId currentHeadId =
                getWriteRepository().resolve(Constants.HEAD);
        boolean commitAvailable = true;
        if (currentHeadId != null) {
            org.eclipse.jgit.lib.Commit headCommit =
                    writeRepository.mapCommit(currentHeadId);
            if (headCommit != null) {
                Tree headTree = headCommit.getTree();
                if (headTree != null && head.getId().equals(headTree.getId())) {
                    commitAvailable = false;
                }
            }
        }
        if (commitAvailable || getConfig().isAllowNoChangeCommit()) {
            performCommit(commit, head);
        }
    }

    protected Set<ObjectId> getGraphForResourceId(String resourceId)
            throws MissingObjectException,
            IncorrectObjectTypeException,
            IOException {
        final Set<ObjectId> versions = new LinkedHashSet<ObjectId>();
        final RevWalk rw = new RevWalk(getReadRepository());
        final TreeWalk tw = new TreeWalk(getReadRepository());
        rw.markStart(rw.parseCommit(getReadRepository().resolve(Constants.HEAD)));
        tw.setFilter(TreeFilter.ANY_DIFF);
        RevCommit c;
        while ((c = rw.next()) != null) {
            final ObjectId[] p = new ObjectId[c.getParentCount() + 1];
            for (int i = 0; i < c.getParentCount(); i++) {
                rw.parseAny(c.getParent(i));
                p[i] = c.getParent(i).getTree();
            }
            final int me = p.length - 1;
            p[me] = c.getTree();
            tw.reset(p);
            while (tw.next()) {
                if (tw.getFileMode(me).getObjectType() == Constants.OBJ_BLOB) {
                    // This path was modified relative to the ancestor(s)
                    String s = tw.getPathString();
                    if (s != null && s.equals(resourceId)) {
                        versions.add(tw.getObjectId(me));
                    }
                }
                if (tw.isSubtree()) {
                    // make sure we recurse into modified directories
                    tw.enterSubtree();
                }
            }
        }
        return versions;
    }
}
