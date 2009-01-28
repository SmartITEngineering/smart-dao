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
package com.smartitengineering.version.impl.jgit.service;

/**
 *
 * @author imyousuf
 */
public class RCSConfig {

    private int concurrentWriteOperations;
    private boolean allowNoChangeCommit;
    private String repositoryPath;

    public boolean isAllowNoChangeCommit() {
        return allowNoChangeCommit;
    }

    public void setAllowNoChangeCommit(boolean allowNoChangeCommit) {
        this.allowNoChangeCommit = allowNoChangeCommit;
    }

    public int getConcurrentWriteOperations() {
        return concurrentWriteOperations;
    }

    public void setConcurrentWriteOperations(int concurrentWriteOperations) {
        this.concurrentWriteOperations = concurrentWriteOperations;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }
}
