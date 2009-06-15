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
 * A config scanner for package
 * @author imyousuf
 * @since 0.4
 */
public interface PackageConfigScanner
    extends ConfigScanner {

    /**
     * Scans and prepares all configurations in the package and makes it
     * available for future use.
     * @param resourcePackage Package to scan and gather configuration
     * @throws IllegalArgumentException If package is null
     */
    public void scanPackageForResourceConfigs(final Package resourcePackage);
}
