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
package com.smartitengineering.exim.impl;

import com.smartitengineering.exim.AssociationConfig;

/**
 * Default implementation of {@link AssociationConfig} for representing an
 * association's configuraiton
 * @author imyousuf
 * @since 0.4
 */
public class AssociationConfigImpl implements AssociationConfig{
    private AssociationType associationType;
    private boolean itToBeExportedAsUri;
    private boolean transientAssociation;
    private boolean eagerSet;
    private boolean stringProviderImplemented;
    private String name;

    public void setAssociationType(AssociationType associationType) {
        this.associationType = associationType;
    }

    public void setEagerSet(boolean eagerSet) {
        this.eagerSet = eagerSet;
    }

    public void setItToBeExportedAsUri(boolean itToBeExportedAsUri) {
        this.itToBeExportedAsUri = itToBeExportedAsUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStringProviderImplemented(boolean stringProviderImplemented) {
        this.stringProviderImplemented = stringProviderImplemented;
    }

    public void setTransient(boolean transientAssociation) {
        this.transientAssociation = transientAssociation;
    }

    public boolean isItToBeExportedAsUri() {
        return itToBeExportedAsUri;
    }

    public boolean isTransient() {
        return transientAssociation;
    }

    public AssociationType getAssociationType() {
        return associationType;
    }

    public String getName() {
        return name;
    }

    public boolean isEagerSet() {
        return eagerSet;
    }

    public boolean isStringProviderImplemented() {
        return stringProviderImplemented;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\n");
        toStringBuilder.append("\nassociationType: ");
        toStringBuilder.append(associationType);
        toStringBuilder.append("\nitToBeExportedAsUri: ");
        toStringBuilder.append(itToBeExportedAsUri);
        toStringBuilder.append("\ntransientAssociation: ");
        toStringBuilder.append(transientAssociation);
        toStringBuilder.append("\neagerSet: ");
        toStringBuilder.append(eagerSet);
        toStringBuilder.append("\nstringProviderImplemented: ");
        toStringBuilder.append(stringProviderImplemented);
        toStringBuilder.append("\nname: ");
        toStringBuilder.append(name);
        return toStringBuilder.toString();
    }

}
