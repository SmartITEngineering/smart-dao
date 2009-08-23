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

import com.smartitengineering.exim.AssociationConfig.AssociationType;
import com.smartitengineering.exim.ConfigRegistrar;
import com.smartitengineering.exim.EximResourceConfig;
import com.smartitengineering.exim.Exporter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class XMLExporterImpl
				implements Exporter {

		public boolean isExportSupported(Class clazz,
																		 Type genericType,
																		 Annotation[] annotations) {
				if (clazz == null) {
						return false;
				}
				AssociationType type = AssociationType.getAssociationType(clazz);
				if (type != null && type != AssociationType.TYPE_OBJECT &&
						type != AssociationType.TYPE_ARRAY) {
						return true;
				}
				EximResourceConfig config = ConfigRegistrar.getConfigForClass(
								getComponentClass(clazz));
				if (config != null) {
						return true;
				}
				return false;
		}

		public long getAfterExportLength(Object object,
																		 Class clazz,
																		 Type genericType,
																		 Annotation[] annotations) {
				return -1;
		}

		public void exportObject(Object object,
														 Class clazz,
														 Type genericType,
														 Annotation[] annotations,
														 OutputStream outputStream,
														 Map<Object, List<Object>> headers)
						throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
		}

		public String getMediaType() {
				return "application/xml";
		}

		private Class getComponentClass(Class clazz) {
				if (clazz == null) {
						throw new IllegalArgumentException();
				}
				return clazz.isArray() ? clazz.getComponentType() : clazz;
		}
}
