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

import com.smartitengineering.version.api.factory.VersionAPI;
import com.smartitengineering.version.impl.jgit.service.MetaFactory;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author imyousuf
 */
public class MetaServiceTest
    extends TestCase {

    private boolean finished = false;
    private ApplicationContext applicationContext;

    private boolean isFinished() {
        return finished;
    }

    public MetaServiceTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
        applicationContext =
            new ClassPathXmlApplicationContext(
            "com/smartitengineering/smart-dao/smart-version-jgit/test-app-context.xml");
    }

    public void testDI() {
        assertNotNull(MetaFactory.getInstance().getConfig().getRepositoryPath());
        assertNotNull(VersionAPI.getInstance().getVersionControlDao());
    }
}
