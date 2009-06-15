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

import java.util.Collection;
import java.util.Map;

/**
 * A registrar API that is responsible for scanning configurations. 
 * @author imyousuf
 * @since 0.4
 */
public interface ConfigScanner {

    /**
     * Return all configurations scanned till now
     * @return A domain class - config map
     */
    public Map<Class, EximResourceConfig> getConfigurations();

    /**
     * Returns the collection of classes that are scanned till now. This does
     * not mean that there ain't any other resource it just means that this has
     * been scanned till now.
     * @return Collection of classes whose configurations are available
     */
    public Collection<Class> getConfiguredResourceClasses();
}
