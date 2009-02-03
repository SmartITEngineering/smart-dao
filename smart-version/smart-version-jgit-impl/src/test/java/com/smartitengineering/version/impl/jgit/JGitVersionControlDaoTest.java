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

import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.MatchMode;
import com.smartitengineering.dao.common.queryparam.Order;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.version.api.Commit;
import com.smartitengineering.version.api.Resource;
import com.smartitengineering.version.api.Revision;
import com.smartitengineering.version.api.VersionedResource;
import com.smartitengineering.version.api.dao.VersionControlDao;
import com.smartitengineering.version.api.dao.WriteStatus;
import com.smartitengineering.version.api.dao.WriterCallback;
import com.smartitengineering.version.api.factory.VersionAPI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author imyousuf
 */
public class JGitVersionControlDaoTest
    extends TestCase {

    private VersionControlDao jGitImpl;
    private boolean finished = false;
    private static String firstRevisionId = "";
    private static ApplicationContext applicationContext;

    private boolean isFinished() {
        return finished;
    }

    public JGitVersionControlDaoTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
        if (applicationContext != null) {
            applicationContext =
                new ClassPathXmlApplicationContext(
                "com/smartitengineering/smart-dao/smart-version-jgit/" +
                "test-app-context.xml");
        }
        jGitImpl = VersionAPI.getInstance().getVersionControlDao();
        finished = false;
    }

    public void testStore() {
        final Revision revision =
            VersionAPI.createRevision(VersionAPI.createResource("a/a.xml",
            "Content of a/a"), null);

        jGitImpl.store(VersionAPI.createCommit(Arrays.asList(revision, VersionAPI.createRevision(
            VersionAPI.createResource("b/a.xml", "Content of b/a"),
            null)), null,
            null, "Commit message for A", VersionAPI.createAuthor(
            "Imran M Yousuf", "imran@smartitengineering.com"), null), new WriterCallback() {

            public void handle(Commit commit,
                               WriteStatus status,
                               String comment,
                               Throwable error) {
                if (error != null) {
                    error.printStackTrace();
                }
                assertEquals(WriteStatus.STORE_PASS, status);
                assertNull(error);
                assertNotNull(revision.getRevisionId());
                firstRevisionId = revision.getRevisionId();
                finished = true;
            }
        });
        while (!isFinished()) {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        finished = false;
    }

    public void testReadResource() {
        Resource a = jGitImpl.getResource("a/a.xml");
        assertNotNull(a);
        assertEquals("a/a.xml", a.getId());
        assertEquals("Content of a/a", a.getContent());
        finished = false;
        jGitImpl.store(VersionAPI.createCommit(Arrays.asList(VersionAPI.
            createRevision(VersionAPI.createResource("a/a.xml",
            "UPDATE-1 Content of a/a"),
            null)), null,
            null, "Commit message for A-2", VersionAPI.createAuthor(
            "I M Yousuf", "imyousuf@smartitengineering.com"), null), new WriterCallback() {

            public void handle(Commit commit,
                               WriteStatus status,
                               String comment,
                               Throwable error) {
                if (error != null) {
                    error.printStackTrace();
                }
                assertEquals(status, WriteStatus.STORE_PASS);
                assertNull(error);
                finished = true;
            }
        });
        while (!isFinished()) {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        finished = false;
        Resource b = jGitImpl.getResource("b/a.xml");
        assertEquals("b/a.xml", b.getId());
        assertEquals("Content of b/a", b.getContent());
        a = jGitImpl.getResource("a/a.xml");
        assertEquals("a/a.xml", a.getId());
        assertEquals("UPDATE-1 Content of a/a", a.getContent());
        if (StringUtils.isNotBlank(firstRevisionId)) {
            a = jGitImpl.getResourceByRevision(firstRevisionId, "a/a.xml");
            assertEquals("a/a.xml", a.getId());
            assertEquals("Content of a/a", a.getContent());
        }
        /**
         * Try non-existing resource id and revision id
         */
        String rubbish = "asdassdasd";
        try {
            jGitImpl.getResource(rubbish);
            fail("Should not be able to return");
        }
        catch (RuntimeException ex) {
        }
        try {
            jGitImpl.getResourceByRevision(rubbish, rubbish);
            fail("Should not be able to return");
        }
        catch (RuntimeException ex) {
        }
    }

    public void testRemove() {
        finished = false;
        jGitImpl.remove(VersionAPI.createCommit(Arrays.asList(VersionAPI.
            createRevision(VersionAPI.createResource("b/a.xml", "DELETE"),
            null)), null,
            null, "Commit message for B-2, delete", VersionAPI.createAuthor(
            "Imran M Yousuf", "imran.yousuf@smartitengineering.com"), null), new WriterCallback() {

            public void handle(Commit commit,
                               WriteStatus status,
                               String comment,
                               Throwable error) {
                if (error != null) {
                    error.printStackTrace();
                }
                assertEquals(WriteStatus.STORE_PASS, status);
                assertNull(error);
                finished = true;
            }
        });
        while (!isFinished()) {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        finished = false;
        try {
            jGitImpl.getResource("b/a.xml");
            fail("Should not be able to return");
        }
        catch (RuntimeException ex) {
        }
    }

    public void testVersionedResource() {
        VersionedResource versionedResource = jGitImpl.getVersionedResource(
            "/////a/a.xml///");
        assertNotNull(versionedResource);
        assertNotNull(versionedResource.getHeadVersionResource());
        Resource a = versionedResource.getHeadVersionResource();
        assertEquals("a/a.xml", a.getId());
        assertEquals("UPDATE-1 Content of a/a", a.getContent());
        assertEquals(2, versionedResource.getRevisions().size());
        Iterator<Revision> iterator =
            versionedResource.getRevisions().iterator();
        a = iterator.next().getResource();
        assertEquals("a/a.xml", a.getId());
        assertEquals("UPDATE-1 Content of a/a", a.getContent());
        a = iterator.next().getResource();
        assertEquals("a/a.xml", a.getId());
        assertEquals("Content of a/a", a.getContent());
        try {
            jGitImpl.getVersionedResource("aaaa/a.xml");
            fail("Should not return!");
        }
        catch (RuntimeException exception) {
        }

    }

    public void testSearch() {
        final Collection<QueryParameter> collection;
        collection = new ArrayList<QueryParameter>();
        Collection<Commit> commitResult;
        Collection<Revision> revisionResult;
        /**
         * Search by committer name
         */
        collection.add(QueryParameterFactory.getStringLikePropertyParam(SearchProperties.COMMITTER_NAME.
            getPropertyName(), "I M Yousuf", MatchMode.EXACT));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(1, commitResult.size());
        collection.clear();
        collection.add(QueryParameterFactory.getStringLikePropertyParam(SearchProperties.COMMITTER_NAME.
            getPropertyName(), "Imran", MatchMode.START));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(2, commitResult.size());
        collection.clear();
        collection.add(QueryParameterFactory.getStringLikePropertyParam(SearchProperties.COMMITTER_NAME.
            getPropertyName(), "Yousuf", MatchMode.ANYWHERE));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(3, commitResult.size());
        /**
         * Search by commit date
         */
        collection.add(QueryParameterFactory.getOrderByParam(SearchProperties.COMMIT_DATE.
            getPropertyName(), Order.DESC));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(3, commitResult.size());
        Iterator<Commit> commitIterator = commitResult.iterator();
        commitIterator.next();
        Commit commit = commitIterator.next();
        Date when = commit.getCommitTime();
        collection.clear();
        collection.add(QueryParameterFactory.<Date>getEqualPropertyParam(
            SearchProperties.COMMIT_DATE.getPropertyName(), when));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(1, commitResult.size());
        assertEquals(commit, commitResult.iterator().next());
        collection.clear();
        /**
         * Search by commit message
         */
        collection.clear();
        collection.add(QueryParameterFactory.getStringLikePropertyParam(SearchProperties.COMMIT_MSG.
            getPropertyName(), "A-2", MatchMode.END));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(1, commitResult.size());
        commit = commitResult.iterator().next();
        assertEquals(1, commit.getRevisions().size());
        Resource a = commit.getRevisions().iterator().next().getResource();
        assertEquals("a/a.xml", a.getId());
        assertEquals("UPDATE-1 Content of a/a", a.getContent());
        /**
         * Search by commit revisions
         */
        collection.clear();
        collection.add(QueryParameterFactory.getOrderByParam(SearchProperties.COMMIT_DATE.
            getPropertyName(), Order.DESC));
        collection.add(QueryParameterFactory.getNestedParametersParam(
            SearchProperties.COMMIT_REVISIONS.getPropertyName(), FetchMode.JOIN,
            QueryParameterFactory.getEqualPropertyParam(
            SearchProperties.REVISION_RESOURCE.getPropertyName(), "a/a.xml")));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(2, commitResult.size());
        collection.clear();
        collection.add(QueryParameterFactory.<Boolean>getEqualPropertyParam(
            SearchProperties.REVISION_HEAD.getPropertyName(), true));
        collection.clear();
        collection.add(QueryParameterFactory.getOrderByParam(SearchProperties.COMMIT_DATE.
            getPropertyName(), Order.DESC));
        collection.add(QueryParameterFactory.getNestedParametersParam(
            SearchProperties.COMMIT_REVISIONS.getPropertyName(), FetchMode.JOIN,
            QueryParameterFactory.getEqualPropertyParam(
            SearchProperties.REVISION_RESOURCE.getPropertyName(), "a/a.xml"),
            QueryParameterFactory.getEqualPropertyParam(
            SearchProperties.REVISION_HEAD.getPropertyName(), true)));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(1, commitResult.size());
        /**
         * Search by committer email
         */
        collection.clear();
        collection.add(QueryParameterFactory.getStringLikePropertyParam(SearchProperties.COMMITTER_EMAIL.
            getPropertyName(), "imran@smartitengineering.com", MatchMode.EXACT));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(1, commitResult.size());
        collection.clear();
        collection.add(QueryParameterFactory.getStringLikePropertyParam(SearchProperties.COMMITTER_EMAIL.
            getPropertyName(), "@smartitengineering.com", MatchMode.END));
        commitResult = jGitImpl.searchForCommits(collection);
        assertEquals(3, commitResult.size());
        collection.clear();
        collection.add(QueryParameterFactory.getNestedParametersParam(
            SearchProperties.REVISION_COMMIT.getPropertyName(), FetchMode.JOIN,
            QueryParameterFactory.getStringLikePropertyParam(
            SearchProperties.COMMITTER_EMAIL.getPropertyName(),
            "imran@smartitengineering.com", MatchMode.EXACT)));
        revisionResult = jGitImpl.searchForRevisions(collection);
        assertEquals(2, revisionResult.size());
        /**
         * Search by resource id and others, Also filters by committer email
         */
        collection.add(QueryParameterFactory.getStringLikePropertyParam(
            SearchProperties.REVISION_RESOURCE.getPropertyName(), ".xml",
            MatchMode.END));
        revisionResult = jGitImpl.searchForRevisions(collection);
        assertEquals(2, revisionResult.size());
        collection.add(QueryParameterFactory.getStringLikePropertyParam(
            SearchProperties.REVISION_RESOURCE.getPropertyName(), "a/a.xml",
            MatchMode.EXACT));
        revisionResult = jGitImpl.searchForRevisions(collection);
        assertEquals(1, revisionResult.size());
        /**
         * Apply head filter and delete filter
         */
        collection.clear();
        collection.add(QueryParameterFactory.<Boolean>getEqualPropertyParam(
            SearchProperties.REVISION_HEAD.getPropertyName(), true));
        collection.add(QueryParameterFactory.getEqualPropertyParam(
            SearchProperties.REVISION_RESOURCE_DELETED.getPropertyName(), true));
        revisionResult = jGitImpl.searchForRevisions(collection);
        assertEquals(1, revisionResult.size());
        a = revisionResult.iterator().next().getResource();
        assertEquals("b/a.xml", a.getId());
        collection.clear();
        collection.add(QueryParameterFactory.<Boolean>getEqualPropertyParam(
            SearchProperties.REVISION_HEAD.getPropertyName(), true));
        collection.add(QueryParameterFactory.getEqualPropertyParam(
            SearchProperties.REVISION_RESOURCE_DELETED.getPropertyName(), false));
        revisionResult = jGitImpl.searchForRevisions(collection);
        assertEquals(1, revisionResult.size());
        a = revisionResult.iterator().next().getResource();
        assertEquals("a/a.xml", a.getId());
        assertEquals("UPDATE-1 Content of a/a", a.getContent());
        collection.clear();
        collection.add(QueryParameterFactory.<Boolean>getEqualPropertyParam(
            SearchProperties.REVISION_HEAD.getPropertyName(), true));
        collection.add(QueryParameterFactory.getStringLikePropertyParam(
            SearchProperties.REVISION_RESOURCE.getPropertyName(), "a/a.xml",
            MatchMode.EXACT));
        revisionResult = jGitImpl.searchForRevisions(collection);
        assertEquals(1, revisionResult.size());
        a = revisionResult.iterator().next().getResource();
        assertEquals("a/a.xml", a.getId());
        assertEquals("UPDATE-1 Content of a/a", a.getContent());
        collection.clear();
        collection.add(QueryParameterFactory.<Boolean>getEqualPropertyParam(
            SearchProperties.REVISION_HEAD.getPropertyName(), false));
        collection.add(QueryParameterFactory.getStringLikePropertyParam(
            SearchProperties.REVISION_RESOURCE.getPropertyName(), "a/a.xml",
            MatchMode.EXACT));
        revisionResult = jGitImpl.searchForRevisions(collection);
        assertEquals(1, revisionResult.size());
        a = revisionResult.iterator().next().getResource();
        assertEquals("a/a.xml", a.getId());
        assertEquals("Content of a/a", a.getContent());
    }
}
