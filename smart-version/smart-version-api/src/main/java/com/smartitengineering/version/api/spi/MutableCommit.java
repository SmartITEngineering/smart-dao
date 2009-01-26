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
package com.smartitengineering.version.api.spi;

import com.smartitengineering.version.api.Author;
import com.smartitengineering.version.api.Revision;
import java.util.Collection;
import java.util.Date;

/**
 * It represents a change in the system.
 * @author imyousuf
 */
public interface MutableCommit {

    public void setRevisions(Collection<Revision> revisions);

    public void setCommitId(String commitId);

    public void setCommitMessage(String commitMessage);

    public void setAuthor(Author author);

    public void setCommitTime(Date commitTime);

    public void setParentCommitId(String parentCommitId);
}
