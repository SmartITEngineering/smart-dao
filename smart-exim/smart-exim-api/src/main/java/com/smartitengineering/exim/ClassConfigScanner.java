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
package com.smartitengineering.exim;

/**
 * A config registrar only able to scan classes
 * @author imyousuf
 * @since 0.4
 */
public interface ClassConfigScanner
    extends ConfigScanner {

    /**
     * Retrieves the configuration for the resource class, if its not already
     * generated then it will search the package of the class and generate its
     * configuraiton.
     * @param resourceClass The class to scanClassForConfig retrieve configuration for
     * @return Configuration for the resource; NULL if no resource is available
     *          or generateable
     * @throws IllegalArgumentException If resource class is null!
     */
    public EximResourceConfig getResourceConfigForClass(
        final Class resourceClass);
}
