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

import java.text.ParseException;

/**
 * A simple which allows users to avoid using {@link String#toString()} for
 * determining the string value of an object;
 * @author imyousuf
 * @since 0.4
 */
public interface StringValueProvider {

    /**
     * Formats the current object as a string that it can parse to attain the
     * state it formatted from.
     * @return Formatted form of the object as a string.
     */
    public String format();

    /**
     * Parse the state of this object's instance from a string and attain its
     * current state to it.
     * @param value String value to parse a object's state into itself
     * @throws java.text.ParseException If the string is not formatted as 
     *                                  required. Please check implementors
     *                                  doc for more details
     */
    public void parse(String value) throws ParseException;
}
