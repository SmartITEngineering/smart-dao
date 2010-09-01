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
package com.smartitengineering.dao.impl.hibernate.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import java.util.Date;

/**
 *
 * @author imyousuf
 */
public class Publisher extends AbstractPersistentDTO<Publisher> {
    
    private String name;
    
    private Date establishedDate;
    
    private int numOfEmployees;

    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(Date establishedDate) {
        this.establishedDate = establishedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumOfEmployees() {
        return numOfEmployees;
    }

    public void setNumOfEmployees(Integer numOfEmployees) {
        if(numOfEmployees != null) {
            this.numOfEmployees = numOfEmployees.intValue();
        }
        else {
            this.numOfEmployees = 0;
        }
    }

}
