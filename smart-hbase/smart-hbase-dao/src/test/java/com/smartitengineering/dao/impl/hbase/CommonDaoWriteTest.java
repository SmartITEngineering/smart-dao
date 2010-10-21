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
package com.smartitengineering.dao.impl.hbase;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import com.smartitengineering.dao.hbase.ddl.HBaseTableConfiguration;
import com.smartitengineering.dao.hbase.ddl.HBaseTableGenerator;
import com.smartitengineering.dao.hbase.ddl.config.json.ConfigurationJsonParser;
import com.smartitengineering.dao.impl.hbase.data.SampleDomain;
import com.smartitengineering.dao.impl.hbase.data.SampleDomainObjectCoverter;
import com.smartitengineering.dao.impl.hbase.spi.AsyncExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.DomainIdInstanceProvider;
import com.smartitengineering.dao.impl.hbase.spi.FilterConfigs;
import com.smartitengineering.dao.impl.hbase.spi.LockAttainer;
import com.smartitengineering.dao.impl.hbase.spi.LockType;
import com.smartitengineering.dao.impl.hbase.spi.MergeService;
import com.smartitengineering.dao.impl.hbase.spi.ObjectRowConverter;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import com.smartitengineering.dao.impl.hbase.spi.impl.LockAttainerImpl;
import com.smartitengineering.dao.impl.hbase.spi.impl.MixedExecutorServiceImpl;
import com.smartitengineering.dao.impl.hbase.spi.impl.SchemaInfoProviderBaseConfig;
import com.smartitengineering.dao.impl.hbase.spi.impl.SchemaInfoProviderImpl;
import com.smartitengineering.dao.impl.hbase.spi.impl.guice.GenericBaseConfigProvider;
import com.smartitengineering.dao.impl.hbase.spi.impl.guice.GenericFilterConfigsProvider;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.hadoop.hbase.HBaseTestingUtility;
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
public class CommonDaoWriteTest {

  private static final HBaseTestingUtility TEST_UTIL = new HBaseTestingUtility();
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoWriteTest.class);
  private static Injector injector;
  private static CommonDaos commonDaos;

  @BeforeClass
  public static void globalSetup() throws Exception {
    /*
     * Start HBase and initialize tables
     */
    //-Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl
    System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                       "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
    try {
      TEST_UTIL.startMiniCluster();
    }
    catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
    //Create table for testing
    InputStream classpathResource = CommonDaoWriteTest.class.getClassLoader().getResourceAsStream(
        "com/smartitengineering/dao/impl/hbase/domain/ddl-config-sample.json");
    Collection<HBaseTableConfiguration> configs = ConfigurationJsonParser.getConfigurations(classpathResource);
    try {
      new HBaseTableGenerator(configs, TEST_UTIL.getConfiguration(), true).generateTables();
    }
    catch (Exception ex) {
      LOGGER.error("Could not create table!", ex);
      Assert.fail(ex.getMessage());
    }
    /*
     * Perform injection for different types of common dao
     */
    injector = Guice.createInjector(new TestModule(), new TestNoModule(), new TestOpModule(), new TestPesModule());
    commonDaos = injector.getInstance(CommonDaos.class);
    Assert.assertNotNull("Common Daos not initialized properly!", commonDaos);
  }

  @AfterClass
  public static void globalTearDown() {
    try {
      TEST_UTIL.shutdownMiniCluster();
    }
    catch (Exception ex) {
      LOGGER.warn("Error shutting down!", ex);
    }
  }

  @Test
  public void testOptimisticPersist() {
    com.smartitengineering.dao.common.CommonDao<SampleDomain, Long> dao = commonDaos.optimisticDao;
    SampleDomain domain = new SampleDomain();
    domain.setName("Name 1");
    domain.setId(1l);
    dao.save(domain);
    domain.setName("Name 1 `2");
    try {
      dao.update(domain);
      Assert.fail("Should have failed!");
    }
    catch (Exception ex) {
      LOGGER.info(ex.getMessage(), ex);
    }
    domain.setVersion(1l);
    dao.update(domain);
  }

  @Test
  public void testPessimisticPersist() {
    com.smartitengineering.dao.common.CommonDao<SampleDomain, Long> dao = commonDaos.pessimisticDao;
    SampleDomain domain = new SampleDomain();
    domain.setName("Name 2");
    domain.setId(2l);
    dao.save(domain);
    domain.setName("Name 2 `2");
    try {
      dao.update(domain);
    }
    catch (Exception ex) {
      LOGGER.info(ex.getMessage(), ex);
      Assert.fail("Should not have failed!");
    }
    domain.setVersion(1l);
    dao.update(domain);
  }

  private static class CommonDaos {

    public static final String NO_LOCK = "noLockDao";
    public static final String PESSIMISTIC_LOCK = "pessimisticDao";
    public static final String OPTIMISTIC_LOCK = "optimistic";
    @Inject
    @Named(OPTIMISTIC_LOCK)
    private com.smartitengineering.dao.common.CommonDao<SampleDomain, Long> optimisticDao;
    @Inject
    @Named(NO_LOCK)
    private com.smartitengineering.dao.common.CommonDao<SampleDomain, Long> noLockDao;
    @Inject
    @Named(PESSIMISTIC_LOCK)
    private com.smartitengineering.dao.common.CommonDao<SampleDomain, Long> pessimisticDao;

    public com.smartitengineering.dao.common.CommonDao<SampleDomain, Long> getNoLockDao() {
      return noLockDao;
    }

    public com.smartitengineering.dao.common.CommonDao<SampleDomain, Long> getOptimisticDao() {
      return optimisticDao;
    }

    public com.smartitengineering.dao.common.CommonDao<SampleDomain, Long> getPessimisticDao() {
      return pessimisticDao;
    }
  }
  private static final TypeLiteral<SchemaInfoProviderImpl<SampleDomain, Long>> vTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<SampleDomain, Long>>() {
  };
  private static final TypeLiteral<com.smartitengineering.dao.common.CommonDao<SampleDomain, Long>> l = new TypeLiteral<com.smartitengineering.dao.common.CommonDao<SampleDomain, Long>>() {
  };
  private static final TypeLiteral<CommonDao<SampleDomain, Long>> p = new TypeLiteral<CommonDao<SampleDomain, Long>>() {
  };

  private static class TestModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(new TypeLiteral<FilterConfigs<SampleDomain>>() {
      }).toProvider(new GenericFilterConfigsProvider<SampleDomain>(
          "com/smartitengineering/dao/impl/hbase/domain/Configs.json")).in(Scopes.SINGLETON);
      bind(AsyncExecutorService.class).to(MixedExecutorServiceImpl.class).in(Singleton.class);
      bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
      bind(Integer.class).annotatedWith(Names.named("maxRows")).toInstance(new Integer(100));
      bind(Long.class).annotatedWith(Names.named("waitTime")).toInstance(new Long(10));
      bind(TimeUnit.class).annotatedWith(Names.named("unit")).toInstance(TimeUnit.SECONDS);
      bind(Boolean.class).annotatedWith(Names.named("mergeEnabled")).toInstance(Boolean.FALSE);
      bind(DomainIdInstanceProvider.class).toProvider(Providers.<DomainIdInstanceProvider>of(null));
      bind(new TypeLiteral<MergeService<SampleDomain, Long>>() {
      }).toProvider(Providers.<MergeService<SampleDomain, Long>>of(null));
      bind(new TypeLiteral<Class<Long>>() {
      }).toInstance(Long.class);
    }
  }

  private static class TestOpModule extends PrivateModule {

    @Override
    protected void configure() {
      bind(new TypeLiteral<ObjectRowConverter<SampleDomain>>() {
      }).to(SampleDomainObjectCoverter.class).in(Singleton.class);
      bind(new TypeLiteral<LockAttainer<SampleDomain, Long>>() {
      }).to(new TypeLiteral<LockAttainerImpl<SampleDomain, Long>>() {
      });
      bind(new TypeLiteral<SchemaInfoProviderBaseConfig<SampleDomain>>() {
      }).toProvider(new GenericBaseConfigProvider<SampleDomain>(
          "com/smartitengineering/dao/impl/hbase/domain/BaseConfigOptimistic.json")).in(Scopes.SINGLETON);
      bind(LockType.class).toInstance(LockType.OPTIMISTIC);
      bind(new TypeLiteral<SchemaInfoProvider<SampleDomain, Long>>() {
      }).to(vTypeLiteral).in(Singleton.class);

      bind(l).annotatedWith(Names.named(CommonDaos.OPTIMISTIC_LOCK)).to(p).in(Scopes.SINGLETON);
      binder().expose(l).annotatedWith(Names.named(CommonDaos.OPTIMISTIC_LOCK));
    }
  }

  private static class TestNoModule extends PrivateModule {

    @Override
    protected void configure() {
      bind(new TypeLiteral<ObjectRowConverter<SampleDomain>>() {
      }).to(SampleDomainObjectCoverter.class).in(Singleton.class);
      bind(new TypeLiteral<LockAttainer<SampleDomain, Long>>() {
      }).to(new TypeLiteral<LockAttainerImpl<SampleDomain, Long>>() {
      });
      bind(new TypeLiteral<SchemaInfoProvider<SampleDomain, Long>>() {
      }).to(vTypeLiteral).in(Singleton.class);
      bind(new TypeLiteral<SchemaInfoProviderBaseConfig<SampleDomain>>() {
      }).toProvider(new GenericBaseConfigProvider<SampleDomain>(
          "com/smartitengineering/dao/impl/hbase/domain/BaseConfigNonOptimistic.json")).in(Scopes.SINGLETON);
      bind(LockType.class).toInstance(LockType.NONE);
      bind(l).annotatedWith(Names.named(CommonDaos.NO_LOCK)).to(p).in(Scopes.SINGLETON);
      binder().expose(l).annotatedWith(Names.named(CommonDaos.NO_LOCK));
    }
  }

  private static class TestPesModule extends PrivateModule {

    @Override
    protected void configure() {
      bind(new TypeLiteral<ObjectRowConverter<SampleDomain>>() {
      }).to(SampleDomainObjectCoverter.class).in(Singleton.class);
      bind(new TypeLiteral<LockAttainer<SampleDomain, Long>>() {
      }).to(new TypeLiteral<LockAttainerImpl<SampleDomain, Long>>() {
      });
      bind(new TypeLiteral<SchemaInfoProvider<SampleDomain, Long>>() {
      }).to(vTypeLiteral).in(Singleton.class);
      bind(new TypeLiteral<SchemaInfoProviderBaseConfig<SampleDomain>>() {
      }).toProvider(new GenericBaseConfigProvider<SampleDomain>(
          "com/smartitengineering/dao/impl/hbase/domain/BaseConfigNonOptimistic.json")).in(Scopes.SINGLETON);
      bind(LockType.class).toInstance(LockType.PESSIMISTIC);
      bind(l).annotatedWith(Names.named(CommonDaos.PESSIMISTIC_LOCK)).to(p).in(Scopes.SINGLETON);
      binder().expose(l).annotatedWith(Names.named(CommonDaos.PESSIMISTIC_LOCK));
    }
  }
}
