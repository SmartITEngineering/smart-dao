package com.smartitengineering.dao.impl.search;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.smartitengineering.common.dao.search.CommonFreeTextPersistentDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.domain.PersistentDTO;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit3.JUnit3Mockery;

public class WriteDaoDecoratorTest extends TestCase {

  private final Mockery mockery = new JUnit3Mockery();

  public WriteDaoDecoratorTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(WriteDaoDecoratorTest.class);
  }

  public void testApp() {
    final CommonWriteDao<PersistentDTO> dto = mockery.mock(CommonWriteDao.class);
    final CommonFreeTextPersistentDao<PersistentDTO> dao = mockery.mock(CommonFreeTextPersistentDao.class);
    final PersistentDTO domain = mockery.mock(PersistentDTO.class);
    mockery.checking(new Expectations() {

      {
        Matcher<PersistentDTO[]> matcher = new Matcher<PersistentDTO[]>() {

          @Override
          public boolean matches(Object item) {
            if (item instanceof PersistentDTO[]) {
              PersistentDTO[] dtos = (PersistentDTO[]) item;
              if (dtos.length == 1 && dtos[0] == domain) {
                return true;
              }
            }
            return false;
          }

          @Override
          public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
          }

          @Override
          public void describeTo(Description description) {
          }
        };
        exactly(1).of(dto).save(with(matcher));
        exactly(1).of(dao).save(with(matcher));
        exactly(1).of(dto).save(with(matcher));
        will(throwException(new RuntimeException()));
        exactly(1).of(dto).update(with(matcher));
        exactly(1).of(dao).update(with(matcher));
        exactly(1).of(dto).update(with(matcher));
        will(throwException(new RuntimeException()));
        exactly(1).of(dto).delete(with(matcher));
        exactly(1).of(dao).delete(with(matcher));
        exactly(1).of(dto).delete(with(matcher));
        will(throwException(new RuntimeException()));
      }
    });
    Injector injector = Guice.createInjector(new AbstractModule() {

      @Override
      protected void configure() {
        bind(new TypeLiteral<CommonWriteDao<PersistentDTO>>() {
        }).annotatedWith(Names.named("searchWriteDaoDecoratee")).toInstance(dto);
        bind(new TypeLiteral<CommonFreeTextPersistentDao<PersistentDTO>>() {
        }).toInstance(dao);
      }
    });
    CommonWriteDaoDecorator decorator = injector.getInstance(Key.get(new TypeLiteral<CommonWriteDaoDecorator<PersistentDTO>>() {
    }));
    assertNotNull(decorator);
    decorator.save(domain);
    try {
      decorator.save(domain);
      fail("Should not have passed!");
    }
    catch (RuntimeException ex) {
      //Expected
    }
    decorator.update(domain);
    try {
      decorator.update(domain);
      fail("Should not have passed!");
    }
    catch (RuntimeException ex) {
      //Expected
    }
    decorator.delete(domain);
    try {
      decorator.delete(domain);
      fail("Should not have passed!");
    }
    catch (RuntimeException ex) {
      //Expected
    }
  }
}
