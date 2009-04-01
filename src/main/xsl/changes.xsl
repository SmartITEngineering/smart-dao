<?xml version="1.0" encoding="UTF-8"?>
<!--
 *   
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
 *
 *
-->
<!--
    Document   : changes.xsl
    Created on : March 31, 2009, 12:11 PM
    Author     : imyousuf
    Description:
        Purpose of transformation is to generate a changes.txt file.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" omit-xml-declaration = "yes" indent="no" standalone="yes" media-type="text/html"/>
    <xsl:strip-space elements="*" />
    <xsl:template match="changeset">
        <xsl:element name="html">
            <xsl:element name="body">
                <xsl:element name="h1">
                    <xsl:attribute name="id">header</xsl:attribute>
                    <xsl:text>Changes from </xsl:text>
                    <xsl:value-of select="@startVersion" />
                    <xsl:text> to </xsl:text>
                    <xsl:value-of select="@endVersion" />
                </xsl:element>
                <xsl:element name="br" />
                <xsl:element name="table">
                    <xsl:attribute name="id">data</xsl:attribute>
                    <xsl:apply-templates select="changelog-entry" />
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    <xsl:template match="changelog-entry">
        <xsl:element name="tr">
            <xsl:variable name="message" select="msg" />
            <xsl:element name="td">
                <xsl:value-of select="substring-before($message, '&#x0a;')" />
            </xsl:element>
            <xsl:element name="td">
                <xsl:value-of select="author" />
            </xsl:element>
            <xsl:element name="td">
                <xsl:value-of select="date" />
            </xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
