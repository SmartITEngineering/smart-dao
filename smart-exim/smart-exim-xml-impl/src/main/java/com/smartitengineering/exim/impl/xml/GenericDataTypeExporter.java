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
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Exporter of generic data types;
 * @author imyousuf
 * @since 0.4
 */
public class GenericDataTypeExporter
				extends AbstractElementExporter
				implements ElementExporter {

		private static ElementExporter exporter;

		static {
				exporter = new GenericCollectionElementExporter();
				for (AssociationType type : exporter.getSupportedTypes()) {
						XMLExporterImpl.addHandler(type, exporter);
				}
		}

		public static ElementExporter getInstance() {
				return exporter;
		}

		public GenericDataTypeExporter() {
				Collections.addAll(types, AssociationType.TYPE_DATE,
								AssociationType.TYPE_STRING, AssociationType.TYPE_DOUBLE,
								AssociationType.TYPE_FLOAT, AssociationType.TYPE_INTEGER,
								AssociationType.TYPE_LONG, AssociationType.TYPE_BOOLEAN);
		}

		public void exportElement(AssociationType type,
															Object object,
															TransformerHandler handler)
						throws IOException {
				if (type == null || object == null || handler == null) {
						throw new IOException("All parameters are required!");
				}
				final String elementName = type.getSimpleName();
				final String value;
				switch (type) {
						case TYPE_DATE:
								value = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(
												(Date) object);
								break;
						case TYPE_STRING:
						case TYPE_INTEGER:
						case TYPE_LONG:
						case TYPE_FLOAT:
						case TYPE_DOUBLE:
						case TYPE_BOOLEAN:
						default:
								value = object == null ? null : object.toString();
								break;
				}
				try {
						AttributesImpl atts = new AttributesImpl();
						handler.startElement(EXIM_COLLECN_URI, EXIM_COLLECTION_NS,
										elementName,
										atts);
						if (AssociationType.TYPE_STRING.equals(type)) {
								handler.startCDATA();
						}
						final char[] chars;
						if (StringUtils.isBlank(value)) {
								chars = "".toCharArray();
						}
						else {
								chars = value.toCharArray();
						}
						handler.characters(chars, 0, chars.length);
						if (AssociationType.TYPE_STRING.equals(type)) {
								handler.endCDATA();
						}
						handler.endElement(EXIM_COLLECN_URI, EXIM_COLLECTION_NS, elementName);
				}
				catch (Exception exception) {
						throw new IOException(exception);
				}
		}
}
