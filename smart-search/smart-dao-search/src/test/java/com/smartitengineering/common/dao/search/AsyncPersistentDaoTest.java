/*
 * This is a common dao with basic CRUD operations and is not limited to any
 * persistent layer implementation
 *
 * Copyright (C) 2010  Imran M Yousuf (imyousuf@smartitengineering.com)
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
package com.smartitengineering.common.dao.search;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.smartitengineering.common.dao.search.impl.CommonAsyncFreeTextPersistentDaoImpl;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit3.JUnit3Mockery;

public class AsyncPersistentDaoTest extends TestCase {

  private final Mockery mockery = new JUnit3Mockery();
  private final TypeLiteral<CommonFreeTextPersistentDao<String>> LITERAL =
                                                                 new TypeLiteral<CommonFreeTextPersistentDao<String>>() {
  };

  public void testApp() throws InterruptedException {
    final CommonFreeTextPersistentDao<String> dao = mockery.mock(CommonFreeTextPersistentDao.class);
    Injector injector = Guice.createInjector(new AbstractModule() {

      @Override
      protected void configure() {
        bind(Long.class).annotatedWith(Names.named("saveInterval")).toInstance(1l);
        bind(Long.class).annotatedWith(Names.named("updateInterval")).toInstance(1l);
        bind(Long.class).annotatedWith(Names.named("deleteInterval")).toInstance(1l);
        bind(TimeUnit.class).annotatedWith(Names.named("intervalTimeUnit")).toInstance(TimeUnit.SECONDS);
        bind(LITERAL).annotatedWith(Names.named("primaryFreeTextPersistentDao")).toInstance(dao);
        bind(LITERAL).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<String>>() {
        }).in(Scopes.SINGLETON);
      }
    });
    CommonFreeTextPersistentDao mainDao = injector.getInstance(Key.get(LITERAL));
    assertNotNull(mainDao);
    mockery.checking(new Expectations() {

      {
        exactly(1).of(dao).save(with("test"));
        exactly(1).of(dao).save(with("test 1"));
        will(throwException(new RuntimeException("")));
        exactly(1).of(dao).save(with("test 1"));
        exactly(1).of(dao).update(with("test"));
        exactly(1).of(dao).update(with("test 1"));
        will(throwException(new RuntimeException("")));
        exactly(1).of(dao).update(with("test 1"));
        exactly(1).of(dao).delete(with("test"));
        exactly(1).of(dao).delete(with("test 1"));
        will(throwException(new RuntimeException("")));
        exactly(1).of(dao).delete(with("test 1"));
      }
    });
    mainDao.save("test");
    mainDao.save("test 1");
    mainDao.update("test");
    mainDao.update("test 1");
    mainDao.delete("test");
    mainDao.delete("test 1");
    Thread.sleep(2000);
  }
}
