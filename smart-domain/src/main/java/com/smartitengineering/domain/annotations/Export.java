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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is to mark an associated resource with a policy on how to export, as an
 * URI or object
 * @author imyousuf
 * @since 0.4
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Export {

    /**
     * Set whether to export the annotated associated resource as object or not.
     * By default its true for export as object.
     * @return Whether to export the instance annotated as object or URI
     * @since 0.4
     */
    boolean asObject() default true;

    /**
     * Marks whether the object is transient or not, that is if true the object
     * should be ignored from exporting. Please note that if this is true it
     * will ignore {@link Export#asObject()}.
     * @return True if the object should be exported
     * @since 0.4
     */
    boolean isTransient() default false;
}
