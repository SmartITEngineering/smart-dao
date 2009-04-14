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

/**
 * Its an API for retrieving ID and URI of a domain as String. Its main purpose
 * is for SPI to provide a way to domain providers to provide a way to customize
 * their own need.
 * @author imyousuf
 * @since 0.4
 */
public interface IdentityCustomizer {

    /**
     * Its the operation for client to retrieve the ID as a string of the domain
     * object
     * @return A non-null string representation of the domain's ID.
     */
    public String getIdAsString();

    /**
     * Its the operation for client to retrieve the URI as string of the domain
     * object
     * @return A non-null string representation of the domain's URI.
     */
    public String getUri();
}
