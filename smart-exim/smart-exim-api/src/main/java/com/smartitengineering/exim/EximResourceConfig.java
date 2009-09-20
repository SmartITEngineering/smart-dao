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

import com.smartitengineering.domain.exim.DomainSelfExporter;
import com.smartitengineering.domain.exim.DomainSelfImporter;
import com.smartitengineering.domain.exim.IdentityCustomizer;
import java.util.Map;

/**
 * This API represents the configuration of a class. Two configuration objects
 * are considered to be equal if they represent the same class.
 * @author imyousuf
 * @since 0.4
 */
public interface EximResourceConfig {

    /**
     * Returns the configuration map for the associations of this resource 
     * object class. Since every type in Java extends {@link Object} we consider
     * every member in a resource class to be an association and thus store its
     * configuration in thus manner. Name of the property is the key to the map.
     * @return A property name keyed configuration map of all the associations
     */
    Map<String, AssociationConfig> getAssociationConfigs();

    /**
     * The resource class it generated this configuration from
     * @return Class this configuration represents
     */
    Class getDomainClass();

    /**
     * Returns the property name of the ID property of the domain. Resource
     * objects must either provide a ID property or implement 
     * {@link IdentityCustomizer}. Otherwise the config generator might choose
     * to throw an {@link IllegalArgumentException}.
     * @return Name of the ID property or NULL if it implements {@link IdentityCustomizer}
     */
    String getIdPropertyName();

    /**
     * Returns the prefix to the ID and suffix to the path to resource. It will
     * add '/' in between path and prefix if required.
     * @return Prefix to the ID.
     */
    String getIdPrefix();
    
    /**
     * Return name to be used for exporting, e.g., root node element. It can be
     * overriden in an association by {@link AssociationConfig}'s name.
     * @return Name to be used for export; not null.
     */
    String getName();

    /**
     * Return the path to the resource.
     * @return Path to the resource
     */
    String getPathToResource();

    /**
     * Setting for testing whether to access members by property accessors or
     * field. If access is set by property and a setter is not available that it
     * will be ignored.
     * @return True if access is to done by getter/setter and false if
     *          reflection is to be used for setting property value.
     */
    boolean isAccessByPropertyEnabled();

    /**
     * Configuration for specifying export policy of this object when is in
     * association. 
     * @return True if the resource domain to be exported as URI else inline.
     */
    boolean isAssociateExportPolicyAsUri();

    /**
     * Configuration whether the object is capable to exporting itself or not.
     * @return True if it implements {@link DomainSelfExporter}
     */
    boolean isExporterImplemented();

    /**
     * Defines whether the resource class implements its own ID customizer. It
     * also signifies that it implements its own URI provider.
     * @return True if it implements {@link IdentityCustomizer}
     * @see EximResourceConfig#getIdPropertyName() 
     */
    boolean isIdentityCustomizerImplemented();

    /**
     * Defines whether the resource has its own importer defined for some
     * media types.
     * @return True if it implements {@link DomainSelfImporter} else false
     */
    boolean isImporterImplemented();

    /**
     * Represents the priority of the resource domain. It is applicable only
     * for interfaces. If a non resource domain concrete object implements more
     * than one resource domain annotated interfaces, then based on descending
     * order the priority will popped from a priority queue.
     * @return Priority of this resource domain, 0 by default.
     */
    int getPriority();

		/**
		 * Returns whether basic return types such as String, Integer, Long should
		 * encapsulated in their distinct XML elements or simply as text content.
		 * The reason for this to be true could be for better readability of the XML.
		 * @return True if and only if the basic types are to be encapsulated in
		 *				 their respective element as defined in schema
		 *				 http://www.smartitengineering.com/smart-dao/exim/basic-types
		 */
		boolean isExportBasicTypesInTypeElementEnabled();
}
