/*
 * This is a common dao with basic CRUD operations and is not limited to any
 * persistent layer implementation
 *
 * Copyright (C) 2010  Imran M Yousuf (imyousuf@smartitengineering.com)
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
package com.smartitengineering.dao.solr.impl;

import com.google.inject.Inject;
import com.smartitengineering.dao.solr.ServerConfiguration;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

/**
 *
 * @author imyousuf
 */
public class ServerConfigurationImpl implements ServerConfiguration {

  @Inject
  private String uri;
  @Inject(optional = true)
  private ResponseParser responseParser = new XMLResponseParser();
  @Inject(optional = true)
  private int socketTimeout = 1000;
  @Inject(optional = true)
  private int connectionTimeout = 100;
  @Inject(optional = true)
  private int maxRetries = 1;
  @Inject(optional = true)
  private int defaultMaxConnectionsPerHost = 100;
  @Inject(optional = true)
  private int maxTotalConnections = 100;
  @Inject(optional = true)
  private boolean allowCompression = true;
  @Inject(optional = true)
  private boolean followRedirects = true;

  @Override
  public String getUri() {
    return uri;
  }

  @Override
  public int getSocketTimeout() {
    return socketTimeout;
  }

  @Override
  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  @Override
  public int getDefaultMaxConnectionsPerHost() {
    return defaultMaxConnectionsPerHost;
  }

  @Override
  public int getMaxTotalConnections() {
    return maxTotalConnections;
  }

  @Override
  public boolean isFollowRedirects() {
    return followRedirects;
  }

  @Override
  public boolean isAllowCompression() {
    return allowCompression;
  }

  @Override
  public int getMaxRetries() {
    return maxRetries;
  }

  @Override
  public ResponseParser getResponseParser() {
    return responseParser;
  }

  public void setAllowCompression(boolean allowCompression) {
    this.allowCompression = allowCompression;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setDefaultMaxConnectionsPerHost(int defaultMaxConnectionsPerHost) {
    this.defaultMaxConnectionsPerHost = defaultMaxConnectionsPerHost;
  }

  public void setFollowRedirects(boolean followRedirects) {
    this.followRedirects = followRedirects;
  }

  public void setMaxRetries(int maxRetries) {
    this.maxRetries = maxRetries;
  }

  public void setMaxTotalConnections(int maxTotalConnections) {
    this.maxTotalConnections = maxTotalConnections;
  }

  public void setResponseParser(ResponseParser responseParser) {
    this.responseParser = responseParser;
  }

  public void setSocketTimeout(int socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }
}
