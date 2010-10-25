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
package com.smartitengineering.dao.impl.search;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.smartitengineering.common.dao.search.CommonFreeTextPersistentDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.domain.PersistentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class CommonWriteDaoDecorator<T extends PersistentDTO> implements CommonWriteDao<T> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Inject
  @Named("searchWriteDaoDecoratee")
  private CommonWriteDao<T> primaryWriteDao;
  @Inject
  private CommonFreeTextPersistentDao<T> persistentDao;

  @Override
  public void save(T... states) {
    try {
      primaryWriteDao.save(states);
      persistentDao.save(states);
    }
    catch (RuntimeException ex) {
      logger.warn("Exception from primary decoratee dao while saving! Re-throwing", ex);
      throw ex;
    }
  }

  @Override
  public void update(T... states) {
    try {
      primaryWriteDao.update(states);
      persistentDao.update(states);
    }
    catch (RuntimeException ex) {
      logger.warn("Exception from primary decoratee dao while saving! Re-throwing", ex);
      throw ex;
    }
  }

  @Override
  public void delete(T... states) {
    try {
      primaryWriteDao.delete(states);
      persistentDao.delete(states);
    }
    catch (RuntimeException ex) {
      logger.warn("Exception from primary decoratee dao while saving! Re-throwing", ex);
      throw ex;
    }
  }
}
