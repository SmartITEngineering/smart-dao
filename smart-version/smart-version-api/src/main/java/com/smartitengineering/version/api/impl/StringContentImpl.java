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
package com.smartitengineering.version.api.impl;

import com.smartitengineering.util.simple.IOFactory;
import com.smartitengineering.version.api.Content;
import java.io.InputStream;

/**
 *
 * @author imyousuf
 */
public class StringContentImpl
    implements Content {

    private String content;
    private InputStream inputStream;

    public StringContentImpl(final String content) {
        if (content == null) {
            throw new IllegalArgumentException();
        }
        this.content = content;
    }

    public String getContent()
        throws IllegalStateException {
        return content;
    }

    public InputStream getContentAsStream() {
        if (inputStream == null) {
            this.inputStream = IOFactory.getStringInputStream(this.content);
        }
        return inputStream;
    }

    public int getContentSize() {
        return content.length();
    }

    public boolean isContentLoaded() {
        return true;
    }
}
