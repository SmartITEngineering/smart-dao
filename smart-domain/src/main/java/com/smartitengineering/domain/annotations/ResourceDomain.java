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
package com.smartitengineering.domain.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used to mark a domain object as a resource. If not
 * mentioned then it will not be considered as resource and this in case of
 * composition with a resource duing export it will be exported as a normal
 * object rather than as a resource.
 * @author imyousuf
 * @since 0.4
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceDomain {

    /**
     * The path to prefix the resource's id or its id's path. If "@Path"
     * annotation is specified then it will be ignored
     * @return Path to the resource domain
     * @since 0.4
     */
    String path() default "";

    /**
     * What is the default strategy for exporting this resource domain when its
     * associated with another resource. If true it will be exported as a URI
     * only else it will be exported as an normal object, these will be the
     * default behaviour.
     * @return Whether to export as URI or not
     * @since 0.4
     */
    boolean exportAsURIByDefault() default true;

    /**
     * For marking how to access the attributes of the resource domain. There is
     * only 2 choices offered - access field directly and access through getter.
     * By default its by getters.
     * @return False if intended access to attributes is through their fields.
     * @since 0.4
     */
    boolean accessByProperty() default true;

    /**
     * Since resource domain can be an interface as well, we need a way to
     * specify the priority of an interface in case a concrete class implements
     * more than one interface.
     * @return Priority of this resource domain
     * @since 0.4
     */
    int priority() default 0;
}
