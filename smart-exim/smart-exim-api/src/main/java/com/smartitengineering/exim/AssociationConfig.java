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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A configuration API for all association in an exim-able object. Since in Java
 * all types are of type {@link Object} we will considering every member (a
 * field or a property) an association and thus store a configuration for it.
 * @author imyousuf
 * @since 0.4
 */
public interface AssociationConfig {

    /**
     * Defines whether the associate is to be exported as URI or not.
     * @return True if its expected to export as URI else false
     */
    public boolean isItToBeExportedAsUri();

    /**
     * Defines whether the association is to be ignored for when the domain is
     * exported or imported.
     * @return True if it is to be ignored for Ex-Im.
     */
    public boolean isTransient();

    /**
     * Defines whether the object should be imported eagerly or on demand. It
     * will only be supported if the resource is exported using properties. If
     * any association config has it set then it will use CGLIB for proxying the
     * resources.
     * @return True if association is to be fetched eagerly.
     * @see AssociationConfig#isItToBeExportedAsUri() 
     * @see AssociationConfig#isTransient() 
     */
    public boolean isEagerSet();

    /**
     * Defines the name representing the association in the exported doc.
     * @return Name to be used in export/import; if null then field/property
     *          name will be used instead.
     */
    public String getName();

    /**
     * Defines whether the association has its own string value generator
     * implementation, (if its not exported as URI) if it does have a provider 
     * then it will be used else toString will be used.
     * @return True if it has a string provider implemented.
     * @see AssociationConfig#isItToBeExportedAsUri() 
     */
    public boolean isStringProviderImplemented();

    /**
     * Defines the type of association this configuration represents
     * @return Type of the associaiton
     */
    public AssociationType getAssociationType();

    /**
     * It defimes types that impacts how the data is used in order to be
     * exported.
     */
    public enum AssociationType {

        TYPE_OBJECT(Object.class),
        TYPE_LIST(List.class),
        TYPE_SET(Set.class),
        TYPE_COLLECTION(Collection.class),
        TYPE_ARRAY(Object.class),
        TYPE_MAP(Map.class),;
        private Class rootClass;

        AssociationType(Class rootClass) {
            this.rootClass = rootClass;
        }

        public Class getRootClass() {
            return rootClass;
        }
    }
}
