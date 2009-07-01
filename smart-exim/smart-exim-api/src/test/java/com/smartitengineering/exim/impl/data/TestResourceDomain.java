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
package com.smartitengineering.exim.impl.data;

import com.smartitengineering.domain.annotations.Eager;
import com.smartitengineering.domain.annotations.Export;
import com.smartitengineering.domain.annotations.Id;
import com.smartitengineering.domain.annotations.Name;
import com.smartitengineering.domain.annotations.ResourceDomain;
import com.smartitengineering.domain.exim.DomainSelfExporter;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author imyousuf
 */
@ResourceDomain(path = TestResourceDomain.PATH,
                exportAsURIByDefault = false)
public class TestResourceDomain
    extends SomeResource
    implements DomainSelfExporter {

    public static final String SOME_TRANSIENT_PROP = "someTransientProp";
    public static final String SOME_BOOL_PROP = "someBoolProp";
    public static final String SOME_PROP = "someProp";
    public static final String ID = "id";
    public static final String SOME_PROP_NAME = "somePropName";
    public static final String PATH = "propDomain";
    public static final String ID_PREFIX = "idPrefix";

    @Id(path = TestResourceDomain.ID_PREFIX)
    public Integer getId() {
        return 0;
    }

    @Name(value = TestResourceDomain.SOME_PROP_NAME)
    @Export(asObject=false)
    public String getSomeProp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Export(isTransient=true)
    public String getSomeTransientProp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Eager
    public boolean hasSomeBoolProp() {
        return false;
    }

    public static String getStrInstance() {
        return "";
    }

    private String getPrivateString() {
        return "";
    }

    protected String getProtectedString() {
        return "";
    }

    public boolean isMIMESupported(String mimeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void exportObject(String mimeType,
                             OutputStream outputStream)
        throws IllegalArgumentException,
               IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
