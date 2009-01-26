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

import com.smartitengineering.version.api.Commit;
import com.smartitengineering.version.api.Resource;
import com.smartitengineering.version.api.Revision;
import com.smartitengineering.version.api.VersionedResource;
import com.smartitengineering.version.api.dao.WriteStatus;
import com.smartitengineering.version.api.dao.WriterCallback;
import com.smartitengineering.version.api.factory.VersionAPI;
import java.util.Arrays;
import java.util.Iterator;
import junit.framework.TestCase;

/**
 *
 * @author imyousuf
 */
public class JGitImplTest
    extends TestCase {

    private static final String DEFAULT_PATH = "target/jgit-impl/.git";
    private JGitImpl jGitImpl;

    public JGitImplTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
        jGitImpl = new JGitImpl();
        jGitImpl.setRepositoryLocation(DEFAULT_PATH);
        jGitImpl.init();
    }

    @Override
    protected void tearDown()
        throws Exception {
        super.tearDown();
        jGitImpl.finish();
    }

    public void testStore() {
        jGitImpl.store(VersionAPI.createCommit(Arrays.asList(VersionAPI.
            createRevision(
            VersionAPI.createResource("a/a.xml", "Content of a/a"),
            null), VersionAPI.createRevision(
            VersionAPI.createResource("b/a.xml", "Content of b/a"), null)), null,
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
            }
        });
    }

    public void testReadResource() {
        Resource a = jGitImpl.getResource("a/a.xml");
        assertEquals("a/a.xml", a.getId());
        assertEquals("Content of a/a", a.getContent());
        jGitImpl.store(VersionAPI.createCommit(Arrays.asList(VersionAPI.
            createRevision(VersionAPI.createResource("a/a.xml",
            "UPDATE-1 Content of a/a"),
            null)), null,
            null, "Commit message for A-2", VersionAPI.createAuthor(
            "Imran M Yousuf", "imran@smartitengineering.com"), null), new WriterCallback() {

            public void handle(Commit commit,
                               WriteStatus status,
                               String comment,
                               Throwable error) {
                if (error != null) {
                    error.printStackTrace();
                }
                assertEquals(status, WriteStatus.STORE_PASS);
                assertNull(error);
            }
        });
        Resource b = jGitImpl.getResource("b/a.xml");
        assertEquals("b/a.xml", b.getId());
        assertEquals("Content of b/a", b.getContent());
        a = jGitImpl.getResource("a/a.xml");
        assertEquals("a/a.xml", a.getId());
        assertEquals("UPDATE-1 Content of a/a", a.getContent());
    }

    public void testRemove() {
        jGitImpl.remove(VersionAPI.createCommit(Arrays.asList(VersionAPI.
            createRevision(VersionAPI.createResource("b/a.xml", "DELETE"),
            null)), null,
            null, "Commit message for B-2, delete", VersionAPI.createAuthor(
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
            }
        });
        Resource b = jGitImpl.getResource("b/a.xml");
        assertNull(b);
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
        versionedResource = jGitImpl.getVersionedResource("aaaa/a.xml");
        assertNull(versionedResource);
    }
}
