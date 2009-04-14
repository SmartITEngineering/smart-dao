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
package com.smartitengineering.rs;

import com.smartitengineering.rs.api.Exporter;
import com.smartitengineering.rs.api.Importer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit3.JUnit3Mockery;

/**
 * Unit test for simple App.
 */
public class SmartProviderTest
    extends TestCase {

    private Mockery context = new JUnit3Mockery();
    private Exporter mockExporter;
    private Importer mockImporter;
    private SmartProvider smartProvider;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SmartProviderTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(SmartProviderTest.class);
    }

    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
        mockExporter = context.mock(Exporter.class);
        mockImporter = context.mock(Importer.class);
        smartProvider = new SmartProvider();
    }

    public void testSetters() {
        assertNotNull(smartProvider.getExporters());
        assertNotNull(smartProvider.getImporters());
        context.checking(new Expectations() {

            {
                exactly(1).of(mockExporter).getMediaType();
                will(returnValue(null));
                exactly(2).of(mockExporter).getMediaType();
                will(returnValue(MediaType.APPLICATION_XML_TYPE));
            }
        });
        smartProvider.setExporter(mockExporter);
        assertEquals(smartProvider.getExporters().size(), 0);
        smartProvider.setExporter(mockExporter);
        assertEquals(smartProvider.getExporters().size(), 1);
        smartProvider.setExporter(null);
        assertEquals(smartProvider.getExporters().size(), 1);
        context.checking(new Expectations() {

            {
                exactly(1).of(mockImporter).getMediaType();
                will(returnValue(null));
                exactly(2).of(mockImporter).getMediaType();
                will(returnValue(MediaType.APPLICATION_XML_TYPE));
            }
        });
        smartProvider.setImporter(mockImporter);
        assertEquals(smartProvider.getImporters().size(), 0);
        smartProvider.setImporter(mockImporter);
        assertEquals(smartProvider.getImporters().size(), 1);
        smartProvider.setImporter(null);
        assertEquals(smartProvider.getImporters().size(), 1);
    }

    public void testIsWriteable() {
        initializeProvider();
        context.checking(new Expectations() {

            {
                exactly(2).of(mockExporter).getMediaType();
                will(returnValue(MediaType.APPLICATION_JSON_TYPE));
            }
        });
        smartProvider.setExporter(mockExporter);
        assertEquals(2, smartProvider.getExporters().size());
        context.checking(new Expectations() {

            {
                exactly(1).of(mockExporter).isExportSupported(with(any(
                    Class.class)), with(aNull(Type.class)), (Annotation[]) with(
                    anything()));
                will(returnValue(true));
                exactly(1).of(mockExporter).isExportSupported(with(any(
                    Class.class)), with(aNull(Type.class)), (Annotation[]) with(
                    anything()));
                will(returnValue(false));
            }
        });
        assertTrue(smartProvider.isWriteable(SmartProvider.class, null,
            new Annotation[]{}, MediaType.APPLICATION_XML_TYPE));
        assertFalse(smartProvider.isWriteable(SmartProvider.class, null,
            new Annotation[]{}, MediaType.APPLICATION_JSON_TYPE));
        assertFalse(smartProvider.isWriteable(SmartProvider.class, null,
            new Annotation[]{}, MediaType.APPLICATION_ATOM_XML_TYPE));
    }

    public void testGetSize() {
        initializeProvider();
        context.checking(new Expectations() {

            {
                exactly(1).of(mockExporter).isExportSupported(with(any(
                    Class.class)), with(aNull(Type.class)), (Annotation[]) with(
                    anything()));
                will(returnValue(true));
                exactly(1).of(mockExporter).getAfterExportLength(with(any(
                    Importer.class)), with(any(Class.class)),
                    with(aNull(Type.class)), (Annotation[]) with(anything()));
                will(returnValue(10L));
            }
        });
        assertEquals(10,
            smartProvider.getSize(mockImporter, SmartProvider.class, null,
            new Annotation[0], MediaType.APPLICATION_XML_TYPE));
        assertEquals(-1,
            smartProvider.getSize(mockImporter, SmartProvider.class, null,
            new Annotation[0], MediaType.APPLICATION_JSON_TYPE));
    }

    public void testWriteTo() {
        initializeProvider();
        context.checking(new Expectations() {

            {
                try {
                    exactly(1).of(mockExporter).
                        isExportSupported(with(any(Class.class)), with(aNull(
                        Type.class)), (Annotation[]) with(anything()));
                    will(returnValue(true));
                    exactly(1).of(mockExporter).exportObject(with(any(
                        Importer.class)), with(any(Class.class)), with(aNull(
                        Type.class)), (Annotation[]) with(anything()), with(any(
                        OutputStream.class)), with(any(Map.class)));
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        MultivaluedMap map = context.mock(MultivaluedMap.class);
        try {
            smartProvider.writeTo(mockImporter,
                SmartProvider.class, null, new Annotation[0],
                MediaType.APPLICATION_XML_TYPE, map, new ByteArrayOutputStream());
        }
        catch (IOException ex) {
            fail(ex.getMessage());
        }
        catch (WebApplicationException ex) {
            fail(ex.getMessage());
        }
        try {
            smartProvider.writeTo(mockImporter,
                SmartProvider.class, null, new Annotation[0],
                MediaType.APPLICATION_JSON_TYPE, map,
                new ByteArrayOutputStream());
            fail("Should have failed!");
        }
        catch (IOException ex) {
            assertEquals("Export is supported for this object!", ex.getMessage());
        }
        catch (WebApplicationException ex) {
            fail(ex.getMessage());
        }
    }

    public void testIsReadable() {
        initializeProvider();
        context.checking(new Expectations() {

            {
                exactly(2).of(
                    mockImporter).getMediaType();
                will(returnValue(MediaType.APPLICATION_JSON_TYPE));
            }
        });
        smartProvider.setImporter(mockImporter);
        context.checking(new Expectations() {

            {
                exactly(1).of(
                    mockImporter).isImportSupported(with(any(
                    Class.class)), with(aNull(Type.class)), (Annotation[]) with(
                    anything()));
                will(returnValue(true));
                exactly(1).of(mockImporter).isImportSupported(with(any(
                    Class.class)), with(aNull(Type.class)), (Annotation[]) with(
                    anything()));
                will(returnValue(false));
            }
        });
        assertTrue(smartProvider.isReadable(SmartProvider.class, null,
            new Annotation[]{}, MediaType.APPLICATION_XML_TYPE));
        assertFalse(smartProvider.isReadable(SmartProvider.class, null,
            new Annotation[]{}, MediaType.APPLICATION_JSON_TYPE));
        assertFalse(smartProvider.isReadable(SmartProvider.class, null,
            new Annotation[]{}, MediaType.APPLICATION_ATOM_XML_TYPE));
    }

    public void testReadFrom() {
        initializeProvider();
        context.checking(new Expectations() {

            {
                try {
                    exactly(1).of(mockImporter).isImportSupported(with(any(
                        Class.class)), with(aNull(Type.class)),
                        (Annotation[]) with(anything()));
                    will(returnValue(true));
                    exactly(1).of(mockImporter).importObject(with(any(
                        Class.class)), with(aNull(Type.class)),
                        (Annotation[]) with(anything()), with(any(
                        InputStream.class)), with(any(Map.class)));
                    will(returnValue("Object"));
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        MultivaluedMap map = context.mock(MultivaluedMap.class);
        try {
            Object object = smartProvider.readFrom(SmartProvider.class, null,
                new Annotation[0], MediaType.APPLICATION_XML_TYPE, map,
                new ByteArrayInputStream(new byte[10]));
            assertEquals("Object", object);
        }
        catch (IOException ex) {
            fail(ex.getMessage());
        }
        catch (WebApplicationException ex) {
            fail(ex.getMessage());
        }
        try {
            smartProvider.readFrom(SmartProvider.class, null, new Annotation[0],
                MediaType.APPLICATION_JSON_TYPE, map,
                new ByteArrayInputStream(new byte[10]));
            fail("Should have failed!");
        }
        catch (IOException ex) {
            assertEquals("Import is not supported for this object!", ex.
                getMessage());
        }
        catch (WebApplicationException ex) {
            fail(ex.getMessage());
        }
    }

    private void initializeProvider() {
        context.checking(new Expectations() {

            {
                exactly(2).of(
                    mockExporter).getMediaType();
                will(returnValue(MediaType.APPLICATION_XML_TYPE));
                exactly(2).of(mockImporter).getMediaType();
                will(returnValue(MediaType.APPLICATION_XML_TYPE));
            }
        });
        smartProvider.setExporter(mockExporter);
        smartProvider.setImporter(mockImporter);
    }
}
