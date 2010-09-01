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
package com.smartitengineering.dao.common.cache;

import com.smartitengineering.dao.common.cache.impl.CacheAPIFactory;
import com.smartitengineering.domain.AbstractPersistentDTO;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Is;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit3.JUnit3Mockery;

/**
 * Unit test for simple App.
 */
public class CacheEventRegistrarTest
    extends TestCase {

    Mockery context = new JUnit3Mockery();

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CacheEventRegistrarTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CacheEventRegistrarTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testAddChangeListener() {
        try {
            CacheAPIFactory.getCacheEventRegistrar().addCacheEventListeners(null,
                null);
            fail("Not expected to pass!");
        }
        catch (IllegalArgumentException ex) {
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Only IllegalArgException expected!");
        }
        try {
            CacheAPIFactory.getCacheEventRegistrar().addCacheEventListeners(
                Observee.class,
                null);
            fail("Not expected to pass!");
        }
        catch (IllegalArgumentException ex) {
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Only IllegalArgException expected!");
        }
        try {
            CacheAPIFactory.getCacheEventRegistrar().addCacheEventListeners(null,
                null, new ChangeListener[0]);
            fail("Not expected to pass!");
        }
        catch (IllegalArgumentException ex) {
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Only IllegalArgException expected!");
        }
        try {
            CacheAPIFactory.getCacheEventRegistrar().addCacheEventListeners(
                Observee.class,
                null, new ChangeListener[2]);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("No exception expected!");
        }
    }

    public void testRemoveChangeListener() {
        final ChangeListener allListener = context.mock(ChangeListener.class,
            "all");
        CacheAPIFactory.getCacheEventRegistrar().addCacheEventListeners(
            Observee.class, null, new ChangeListener[]{allListener});
        final ChangeListener delListener = context.mock(ChangeListener.class,
            "del");
        CacheAPIFactory.getCacheEventRegistrar().addCacheEventListeners(
            Observee.class, ChangeEvent.ChangeType.DELETE, new ChangeListener[]{
                delListener
            });
        context.checking(new Expectations() {

            {
                atMost(2).of(allListener).cacheChanged(with(any(
                    ChangeEvent.class)));
                atMost(1).of(delListener).cacheChanged(with(new HasPropertyWithValue<ChangeEvent>(
                    "changeType", new Is(equal(ChangeEvent.ChangeType.DELETE)))));
            }
        });
        final Observee source = new Observee();
        CacheAPIFactory.getCacheEventRegistrar().fireEvent(CacheAPIFactory.
            getChangeEvent(source, ChangeEvent.ChangeType.CREATE));
        CacheAPIFactory.getCacheEventRegistrar().fireEvent(CacheAPIFactory.
            getChangeEvent(source, ChangeEvent.ChangeType.DELETE));
        CacheAPIFactory.getCacheEventRegistrar().removeListeners(delListener,
            allListener);
        CacheAPIFactory.getCacheEventRegistrar().fireEvent(CacheAPIFactory.
            getChangeEvent(source, ChangeEvent.ChangeType.CREATE));
        CacheAPIFactory.getCacheEventRegistrar().fireEvent(CacheAPIFactory.
            getChangeEvent(source, ChangeEvent.ChangeType.DELETE));
    }

    public void testFireEvent() {
        final ChangeListener allListener = context.mock(ChangeListener.class,
            "allFire");
        CacheAPIFactory.getCacheEventRegistrar().addCacheEventListeners(
            Observee.class, null, new ChangeListener[]{allListener});
        final ChangeListener delListener = context.mock(ChangeListener.class,
            "delFire");
        CacheAPIFactory.getCacheEventRegistrar().addCacheEventListeners(
            Observee.class, ChangeEvent.ChangeType.DELETE, new ChangeListener[]{
                delListener
            });
        context.checking(new Expectations() {

            {
                exactly(2).of(allListener).cacheChanged(with(any(
                    ChangeEvent.class)));
                exactly(1).of(delListener).cacheChanged(with(new HasPropertyWithValue<ChangeEvent>(
                    "changeType", new Is(equal(ChangeEvent.ChangeType.DELETE)))));
            }
        });
        final Observee source = new Observee();
        CacheAPIFactory.getCacheEventRegistrar().fireEvent(CacheAPIFactory.
            getChangeEvent(source, ChangeEvent.ChangeType.CREATE));
        CacheAPIFactory.getCacheEventRegistrar().fireEvent(CacheAPIFactory.
            getChangeEvent(source, ChangeEvent.ChangeType.DELETE));
        CacheAPIFactory.getCacheEventRegistrar().removeListeners(allListener,
            delListener);
    }

    private class Observee
        extends AbstractPersistentDTO<Observee> {

        public boolean isValid() {
            return true;
        }
    }
}
