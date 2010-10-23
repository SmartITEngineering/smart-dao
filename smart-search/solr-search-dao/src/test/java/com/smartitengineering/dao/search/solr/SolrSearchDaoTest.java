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
package com.smartitengineering.dao.search.solr;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.smartitengineering.common.dao.search.CommonFreeTextPersistentDao;
import com.smartitengineering.common.dao.search.CommonFreeTextSearchDao;
import com.smartitengineering.common.dao.search.solr.SolrFreeTextPersistentDao;
import com.smartitengineering.common.dao.search.solr.SolrFreeTextSearchDao;
import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.ServerConfiguration;
import com.smartitengineering.dao.solr.ServerFactory;
import com.smartitengineering.dao.solr.SolrQueryDao;
import com.smartitengineering.dao.solr.SolrWriteDao;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.dao.solr.impl.ServerConfigurationImpl;
import com.smartitengineering.dao.solr.impl.SingletonRemoteServerFactory;
import com.smartitengineering.dao.solr.impl.SolrDao;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;
import com.smartitengineering.util.bean.adapter.GenericAdapter;
import com.smartitengineering.util.bean.adapter.GenericAdapterImpl;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class SolrSearchDaoTest {

  private static final int PORT = 10080;
  private static Server jettyServer;
  private static CommonFreeTextPersistentDao<Domain> writeDao;
  private static CommonFreeTextSearchDao<Domain> readDao;
  private static final Logger LOGGER = LoggerFactory.getLogger(SolrSearchDaoTest.class);

  @BeforeClass
  public static void globalSetup() throws Exception {
    System.setProperty("solr.solr.home", "./target/sample-conf/");
    jettyServer = new Server(PORT);
    final String webapp = "./target/solr/";
    if (!new File(webapp).exists()) {
      throw new IllegalStateException("WebApp file/dir does not exist!");
    }
    WebAppContext webAppHandler = new WebAppContext(webapp, "/");
    jettyServer.setHandler(webAppHandler);
    jettyServer.setSendDateHeader(true);
    jettyServer.start();
    Injector injector = Guice.createInjector(new SearchModule());
    writeDao = injector.getInstance(Key.get(new TypeLiteral<CommonFreeTextPersistentDao<Domain>>() {
    }));
    readDao = injector.getInstance(Key.get(new TypeLiteral<CommonFreeTextSearchDao<Domain>>() {
    }));
  }

  @AfterClass
  public static void globalTearDown() throws Exception {
    jettyServer.stop();
  }

  @Test
  public void testSimpleAdd() {
    Domain domain = new Domain();
    domain.id = Long.toString(1l);
    domain.name = "Test Domain 1";
    domain.features = new String[]{"sports", "soccer"};
    writeDao.save(domain);
    Collection<Domain> domains = readDao.search(QueryParameterFactory.getStringLikePropertyParam("q", "id: 1"));
    Assert.assertNotNull(domains);
    Assert.assertEquals(1, domains.size());
    domain = domains.iterator().next();
    LOGGER.info("Domain retrieved from search is: " + domain);
    Assert.assertEquals(new Long(1), Long.valueOf(domain.id));
    Assert.assertEquals("Test Domain 1", domain.name);
    Assert.assertTrue(Arrays.equals(new String[]{"sports", "soccer"}, domain.features));
  }

  private static class SearchModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(Long.class).annotatedWith(Names.named("waitTime")).toInstance(new Long(10));
      bind(TimeUnit.class).annotatedWith(Names.named("waitTimeUnit")).toInstance(TimeUnit.SECONDS);
      bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
      bind(new TypeLiteral<ObjectIdentifierQuery<Domain>>() {
      }).to(DomainIdentifierQuery.class).in(Scopes.SINGLETON);
      bind(new TypeLiteral<GenericAdapter<Domain, MultivalueMap<String, Object>>>() {
      }).to(new TypeLiteral<GenericAdapterImpl<Domain, MultivalueMap<String, Object>>>() {
      }).in(Scopes.SINGLETON);
      bind(new TypeLiteral<AbstractAdapterHelper<Domain, MultivalueMap<String, Object>>>() {
      }).to(DomainAdapterHelper.class).in(Scopes.SINGLETON);
      bind(SolrQueryDao.class).to(SolrDao.class).in(Scopes.SINGLETON);
      bind(SolrWriteDao.class).to(SolrDao.class).in(Scopes.SINGLETON);
      bind(ServerFactory.class).to(SingletonRemoteServerFactory.class).in(Scopes.SINGLETON);
      bind(ServerConfiguration.class).to(ServerConfigurationImpl.class).in(Scopes.SINGLETON);
      bind(String.class).annotatedWith(Names.named("uri")).toInstance("http://localhost:10080/");
      bind(new TypeLiteral<CommonFreeTextPersistentDao<Domain>>() {
      }).to(new TypeLiteral<SolrFreeTextPersistentDao<Domain>>() {
      }).in(Scopes.SINGLETON);
      bind(new TypeLiteral<CommonFreeTextSearchDao<Domain>>() {
      }).to(new TypeLiteral<SolrFreeTextSearchDao<Domain>>() {
      }).in(Scopes.SINGLETON);
    }
  }

  private static class Domain {

    String id;
    String name;
    String[] features;

    @Override
    public String toString() {
      return "Domain{" + "id=" + id + ", name=" + name + ", features=" + Arrays.toString(features) + '}';
    }
  }

  private static class DomainAdapterHelper extends AbstractAdapterHelper<Domain, MultivalueMap<String, Object>> {

    @Override
    protected MultivalueMap<String, Object> newTInstance() {
      return new MultivalueMapImpl<String, Object>();
    }

    @Override
    protected void mergeFromF2T(Domain fromBean,
                                MultivalueMap<String, Object> toBean) {
      toBean.addValue("id", fromBean.id);
      toBean.addValue("name", fromBean.name);
      for (String feature : fromBean.features) {
        toBean.addValue("features", feature);
      }
    }

    @Override
    protected Domain convertFromT2F(MultivalueMap<String, Object> toBean) {
      Domain domain = new Domain();
      domain.id = toBean.getFirst("id").toString();
      domain.name = toBean.getFirst("name").toString();
      domain.features = toBean.get("features").toArray(new String[0]);
      return domain;
    }
  }

  private static class DomainIdentifierQuery implements ObjectIdentifierQuery<Domain> {

    @Override
    public String getQuery(Domain object) {
      return "id: " + object.id;
    }
  }
}
