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
package com.smartitengineering.rs.api;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;

/**
 * This API provides SPIs to use it to export objects to a stream of media type
 * it specifies. It is the responsibility of the API client to invoke it for
 * appropriate {@link MediaType}. API implementors should explicitly mention the
 * media type it supports. API implementors should also ensure thread safety.
 * @author imyousuf
 * @since 0.4
 */
public interface Exporter {

    /**
     * An operation that checks whether the the object type qualified by its
     * class is supported by this exporter. This operation should be consulted
     * before invoking/using exportObject.
     * @param clazz The class quantifying the object type
     * @param genericType The generic applied to the object, if any
     * @param annotations Annotations used on the class
     * @return True if and only if export is supported else false.
     */
    boolean isExportSupported(Class clazz,
                              Type genericType,
                              Annotation[] annotations);

    /**
     * It provides the operation to predict the length of the exported content.
     * @param object The object to export
     * @param clazz The class of the object
     * @param genericType The generic used with the object.
     * @param annotations Annotations qualifying the object's class.
     * @return -1 if length can not be predicted or else the actual length after
     *          export
     */
    long getAfterExportLength(Object object,
                              Class clazz,
                              Type genericType,
                              Annotation[] annotations);

    /**
     * Export the object quantified by clazz and genericType and qualified by
     * the annotaitons to the outpuStream and use the headers to set meta
     * informations regarding the object.
     * @param object The object to export
     * @param clazz The class of the object
     * @param genericType The generic used with the object.
     * @param annotations Annotations qualifying the object's class.
     * @param outputStream The output stream to write and format the object to.
     * @param headers The headers that holds the meta information
     * @throws java.io.IOException If any error in writing to the stream, i.e.
     *                             either format error or any other error.
     */
    void exportObject(Object object,
                      Class clazz,
                      Type genericType,
                      Annotation[] annotations,
                      OutputStream outputStream,
                      Map<Object, List<Object>> headers)
        throws IOException;

    /**
     * Provides the {@link MediaType} that is supported by this exporter.
     * @return The supported media type
     */
    MediaType getMediaType();
}
