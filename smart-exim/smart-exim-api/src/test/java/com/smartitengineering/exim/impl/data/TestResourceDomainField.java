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

import com.smartitengineering.domain.annotations.Export;
import com.smartitengineering.domain.annotations.Name;
import com.smartitengineering.domain.annotations.ResourceDomain;
import com.smartitengineering.domain.exim.DomainSelfImporter;
import com.smartitengineering.domain.exim.IdentityCustomizer;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author imyousuf
 */
@ResourceDomain(accessByProperty=false)
@Name(value=TestResourceDomainField.NAME)
public class TestResourceDomainField implements IdentityCustomizer, DomainSelfImporter {

    public static final String SOME_STR_PROP = "someStrProp";
    public static final String SOME_TRANSIENT_STR_PROP = "someTransientStrProp";
    public static final String NAME = "fieldDom";

    private String someStrProp;
    @Export(isTransient=true)
    private String someTransientStrProp;

    public String getIdAsString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getUri() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isMIMESupported(String mimeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void importObject(String mimeType,
                             InputStream inputStream)
        throws IllegalArgumentException,
               IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
