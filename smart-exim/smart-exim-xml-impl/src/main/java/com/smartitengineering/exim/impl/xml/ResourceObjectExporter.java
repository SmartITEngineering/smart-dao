/*
 * This is a common dao with basic CRUD operations and is not limited to any
 * persistent layer implementation
 *
 * Copyright (C) 2009 Imran M Yousuf (imyousuf@smartitengineering.com)
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
import java.io.IOException;
import java.util.Collections;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.commons.lang.StringUtils;

/**
 * This exporter is for {@link AssociationType#TYPE_OBJECT}. For associations of
 * that type it will simply export it as usual but for resource domains it will
 * export it with respect to its configuration.
 * @author imyousuf
 * @since 0.4
 */
public class ResourceObjectExporter
				extends AbstractElementExporter
				implements ElementExporter {

		private static ElementExporter exporter;

		static {
				exporter = new MapElementExporter();
				for (AssociationType type : exporter.getSupportedTypes()) {
						XMLExporterImpl.addHandler(type, exporter);
				}
		}

		public static ElementExporter getInstance() {
				return exporter;
		}

		public ResourceObjectExporter() {
				Collections.addAll(types, AssociationType.TYPE_OBJECT);
		}

		public void exportElement(AssociationType type,
															Object object,
															TransformerHandler handler)
						throws IOException {
				if (type == null || object == null || handler == null) {
						throw new IOException("All parameters are required!");
				}
				EximResourceConfig config = ConfigRegistrar.getConfigForClass(object.
								getClass());
				if(config == null) {
						try {
								handler.startCDATA();
								String value = object == null ? null : object.toString();
								if(StringUtils.isBlank(value)) {
										value = "";
								}
								char[] chars = value.toCharArray();
								handler.characters(chars, 0, chars.length);
								handler.endCDATA();
						}
						catch (Exception ex) {
								throw new IOException(ex);
						}
				}
				else {
						//Handle resources
				}
		}
}
