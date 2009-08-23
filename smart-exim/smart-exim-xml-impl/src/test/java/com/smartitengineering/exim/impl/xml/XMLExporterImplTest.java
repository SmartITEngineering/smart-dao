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
package com.smartitengineering.exim.impl.xml;

import com.smartitengineering.exim.Exporter;
import com.smartitengineering.exim.impl.xml.data.SimpleDomain;
import com.smartitengineering.exim.impl.xml.data.TestDomain;
import java.util.Collection;
import javax.ws.rs.core.MediaType;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for XML Exporter Implementation.
 */
public class XMLExporterImplTest
				extends TestCase {

		Exporter exporter;

		/**
		 * Create the test case
		 *
		 * @param testName name of the test case
		 */
		public XMLExporterImplTest(String testName) {
				super(testName);
		}

		/**
		 * @return the suite of tests being tested
		 */
		public static Test suite() {
				return new TestSuite(XMLExporterImplTest.class);
		}

		@Override
		protected void setUp()
						throws Exception {
				super.setUp();
				exporter = new XMLExporterImpl();
		}

		public void testIsExportedSupported() {
				assertTrue(exporter.isExportSupported(TestDomain.class, null, null));
				assertTrue(exporter.isExportSupported(Collection.class, null, null));
				assertTrue(exporter.isExportSupported(TestDomain[].class, null, null));
				assertFalse(exporter.isExportSupported(null, null, null));
				assertFalse(exporter.isExportSupported(SimpleDomain.class, null, null));
		}

		public void testGetMediaType() {
				assertEquals(MediaType.APPLICATION_XML, exporter.getMediaType());
		}

		public void testGetAfterExportLength() {
				assertEquals(-1, exporter.getAfterExportLength(this, null, null, null));
		}
}
