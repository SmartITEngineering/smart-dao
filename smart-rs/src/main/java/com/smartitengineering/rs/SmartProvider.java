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

import com.smartitengineering.exim.Exporter;
import com.smartitengineering.exim.Importer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * This is the provider for XML and JSON output/input. It uses {@link Exporter}
 * and {@link Importer} to format and parse. Each of the implementation will be
 * responsible for only a specific {@link MediaType}.
 * @author imyousuf
 * @since 0.4
 */
@Provider
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class SmartProvider
    implements MessageBodyWriter,
               MessageBodyReader {

    private Map<MediaType, Exporter> exporters;
    private Map<MediaType, Importer> importers;
    

    {
        exporters = new HashMap<MediaType, Exporter>();
        importers = new HashMap<MediaType, Importer>();
    }

    public boolean isWriteable(Class type,
                               Type genericType,
                               Annotation[] annotations,
                               MediaType mediaType) {
        boolean found = isExportSupported(mediaType);
        if (found) {
            Exporter exporter = getExporters().get(mediaType);
            boolean exportable = exporter.isExportSupported(type, genericType,
                annotations);
            return exportable;
        }
        else {
            return false;
        }
    }

    public long getSize(Object t,
                        Class type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType) {
        boolean found = isWriteable(type, genericType, annotations, mediaType);
        if (found) {
            Exporter exporter = getExporters().get(mediaType);
            return exporter.getAfterExportLength(t, type, genericType,
                annotations);
        }
        else {
            return -1;
        }
    }

    public void writeTo(Object t,
                        Class type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap httpHeaders,
                        OutputStream entityStream)
        throws IOException,
               WebApplicationException {
        boolean found = isWriteable(type, genericType, annotations, mediaType);
        if (found) {
            Exporter exporter = getExporters().get(mediaType);
            exporter.exportObject(t, type, genericType, annotations,
                entityStream,
                httpHeaders);
        }
        else {
            throw new IOException("Export is supported for this object!");
        }
    }

    public boolean isReadable(Class type,
                              Type genericType,
                              Annotation[] annotations,
                              MediaType mediaType) {
        final boolean found = isImportSupported(mediaType);
        if (found) {
            Importer importer = getImporters().get(mediaType);
            return importer.isImportSupported(type, genericType, annotations);
        }
        else {
            return false;
        }
    }

    public Object readFrom(Class type,
                           Type genericType,
                           Annotation[] annotations,
                           MediaType mediaType,
                           MultivaluedMap httpHeaders,
                           InputStream entityStream)
        throws IOException,
               WebApplicationException {
        final boolean found = isReadable(type, genericType, annotations,
            mediaType);
        if (found) {
            Importer importer = getImporters().get(mediaType);
            return importer.importObject(type, genericType, annotations,
                entityStream, httpHeaders);
        }
        else {
            throw new IOException("Import is not supported for this object!");
        }
    }

    protected boolean isExportSupported(MediaType mediaType) {
        boolean found = false;
        for (MediaType keyType : getExporters().keySet()) {
            if (keyType.equals(mediaType)) {
                found = true;
            }
        }
        return found;
    }

    protected boolean isImportSupported(MediaType mediaType) {
        boolean found = false;
        for (MediaType keyType : getImporters().keySet()) {
            if (keyType.equals(mediaType)) {
                found = true;
            }
        }
        return found;
    }

    public void setExporter(Exporter exporter) {
        if (exporter == null || exporter.getMediaType() == null) {
            return;
        }
        String mediaTypeStr = exporter.getMediaType();
        String[] types = mediaTypeStr.split("/");
        MediaType mediaType = new MediaType(types[0], types[1]);
        getExporters().put(mediaType, exporter);
    }

    public void setImporter(Importer importer) {
        if (importer == null || importer.getMediaType() == null) {
            return;
        }
        String mediaTypeStr = importer.getMediaType();
        String[] types = mediaTypeStr.split("/");
        MediaType mediaType = new MediaType(types[0], types[1]);
        getImporters().put(mediaType, importer);
    }

    public Map<MediaType, Exporter> getExporters() {
        return exporters;
    }

    public Map<MediaType, Importer> getImporters() {
        return importers;
    }
}
