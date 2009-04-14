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
package com.smartitengineering.domain.exim;

import java.io.IOException;
import java.io.InputStream;

/**
 * As the name suggests the API indicates that a domain is able to import itself
 * and to determine for which MIME types it can it has an operation dedicated op
 * for it.
 * @author imyousuf
 * @since 0.4
 */
public interface DomainSelfImporter {

    /**
     * It provides the operation for clients to determine whether the domain is
     * able to import itself to the MIME type the client wants.
     * @param mimeType Type to import from
     * @return True if and only if the mimeType is supported for import
     */
    public boolean isMIMESupported(String mimeType);

    /**
     * Imports the domain from the specified stream in specified type
     * @param mimeType Type to import from.
     * @param outputStream Stream to import from.
     * @throws IllegalArgumentException If either argument is null or MIME type
     *                                  isn't supported.
     * @throws IOException If any error occurrs while writing to stream.
     */
    public void importObject(String mimeType,
                             InputStream inputStream)
        throws IllegalArgumentException,
               IOException;
}
