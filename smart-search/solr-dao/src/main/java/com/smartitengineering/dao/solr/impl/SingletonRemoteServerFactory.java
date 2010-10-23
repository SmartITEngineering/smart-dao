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
import com.google.inject.Singleton;
import com.smartitengineering.dao.solr.ServerConfiguration;
import com.smartitengineering.dao.solr.ServerFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
@Singleton
public class SingletonRemoteServerFactory implements ServerFactory {

  @Inject
  private ServerConfiguration configuration;
  private SolrServer solrServer;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public ServerConfiguration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(ServerConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public SolrServer getSolrServer() {
    if (solrServer == null) {
      try {
        HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
        CommonsHttpSolrServer commonsHttpClientSolrServer = new CommonsHttpSolrServer(configuration.getUri(), client);
        commonsHttpClientSolrServer.setAllowCompression(configuration.isAllowCompression());
        commonsHttpClientSolrServer.setConnectionTimeout(configuration.getConnectionTimeout());
        commonsHttpClientSolrServer.setDefaultMaxConnectionsPerHost(configuration.getDefaultMaxConnectionsPerHost());
        commonsHttpClientSolrServer.setFollowRedirects(configuration.isFollowRedirects());
        commonsHttpClientSolrServer.setMaxRetries(configuration.getMaxRetries());
        commonsHttpClientSolrServer.setMaxTotalConnections(configuration.getMaxTotalConnections());
        commonsHttpClientSolrServer.setParser(configuration.getResponseParser());
        commonsHttpClientSolrServer.setSoTimeout(configuration.getSocketTimeout());
        commonsHttpClientSolrServer.setRequestWriter(new BinaryRequestWriter());
        this.solrServer = commonsHttpClientSolrServer;
      }
      catch (Exception ex) {
        logger.error("Could not intialize solr server", ex);
      }
    }
    return solrServer;
  }
}
