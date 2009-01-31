/*
 * This is a common dao with basic CRUD operations and is not limited to any 
 * persistent layer implementation
 * 
 * Copyright (C) 2008  Imran M Yousuf
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.smartitengineering.dao.common;

import com.smartitengineering.domain.PersistentDTO;
import java.util.List;

/**
 *
 * @author Imran M Yousuf
 */
public interface CommonReadDaoWithMap<Template extends PersistentDTO> {

    public Template getSingle(List<QueryParameter> query);

    public List<Template> getList(List<QueryParameter> query);

    public <OtherTemplate extends Object> OtherTemplate getOther(
        List<QueryParameter> query);

    public <OtherTemplate extends Object> List<OtherTemplate> getOtherList(
        List<QueryParameter> query);
}
