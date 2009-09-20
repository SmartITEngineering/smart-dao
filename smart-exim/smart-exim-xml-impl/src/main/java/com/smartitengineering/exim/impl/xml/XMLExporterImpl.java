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
import com.smartitengineering.exim.Exporter;
import com.smartitengineering.util.simple.IOFactory;
import com.smartitengineering.util.simple.reflection.ClassInstanceVisitorImpl;
import com.smartitengineering.util.simple.reflection.ClassScanner;
import com.smartitengineering.util.simple.reflection.Config;
import com.smartitengineering.util.simple.reflection.VisitCallback;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

/**
 *
 * @author imyousuf
 * @since 0.4
 */
public class XMLExporterImpl
				implements Exporter {

		private String encoding;
		private String indent;
		private String method;
		private static Map<AssociationType, ElementExporter> handlers;

		static {
				handlers = new HashMap<AssociationType, ElementExporter>(
								AssociationType.values().length);
				String packageName = ConfigRegistrar.class.getPackage().getName();
				ClassScanner scanner = IOFactory.getDefaultClassScanner();
				scanner.scan(new String[] {packageName},
								new ClassInstanceVisitorImpl(IOFactory.getClassNameForVisitor(
								ElementExporter.class), new VisitCallback<Config>() {

						public void handle(Config config) {
								try {
										IOFactory.getClassFromVisitorName(config.getClassName());
								}
								catch (Exception ex) {
										ex.printStackTrace();
								}
						}
				}));
		}

		static void addHandler(AssociationType type,
													 ElementExporter exporter) {
				handlers.put(type, exporter);
		}

		static ElementExporter getElementExporter(AssociationType type) {
				return handlers.get(type);
		}

		public XMLExporterImpl() {
				encoding = "UTF-8";
				indent = "yes";
				method = "xml";
		}

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
				if (outputStream == null || clazz == null || object == null) {
						throw new IOException(
										"OutputStream or Object or Class can't be null!");
				}
				try {
						SAXTransformerFactory factory =
																	(SAXTransformerFactory) SAXTransformerFactory.
										newInstance();
						TransformerHandler handler = factory.newTransformerHandler();
						StreamResult result = new StreamResult(outputStream);
						Transformer transformer = handler.getTransformer();
						transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
						transformer.setOutputProperty(OutputKeys.INDENT, indent);
						transformer.setOutputProperty(OutputKeys.METHOD, method);
						handler.setResult(result);
						handler.startDocument();
						try {
								AssociationType type = AssociationType.getAssociationType(clazz);
								ElementExporter exporter = getElementExporter(type);
								if (exporter == null) {
										throw new UnsupportedOperationException("Type not supported yet! - " +
																														type);
								}
								exporter.exportElement(type, object, handler);

						}
						finally {
								handler.endDocument();
						}
				}
				catch (SAXException ex) {
						throw new IOException(ex);
				}
				catch (TransformerConfigurationException ex) {
						throw new IOException(ex);
				}
		}

		public String getMediaType() {
				return "application/xml";
		}

		public String getEncoding() {
				return encoding;
		}

		public void setEncoding(String encoding) {
				this.encoding = encoding;
		}

		public String getIndent() {
				return indent;
		}

		public void setIndent(String indent) {
				this.indent = indent;
		}

		public String getMethod() {
				return method;
		}

		public void setMethod(String method) {
				this.method = method;
		}

		private Class getComponentClass(Class clazz) {
				if (clazz == null) {
						throw new IllegalArgumentException();
				}
				return clazz.isArray() ? clazz.getComponentType() : clazz;
		}
}
