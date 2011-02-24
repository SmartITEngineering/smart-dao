/*
 * This is a common dao with basic CRUD operations and is not limited to any
 * persistent layer implementation
 *
 * Copyright (C) 2011  Imran M Yousuf (imyousuf@smartitengineering.com)
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
import com.smartitengineering.common.dao.search.SearchResult;
import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.ServerFactory;
import com.smartitengineering.dao.solr.SolrQueryDao;
import com.smartitengineering.dao.solr.SolrWriteDao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If this dao is used commit/rollback invocation has to be made explicitly
 * @author imyousuf
 */
public class DefaultSolrDao implements SolrWriteDao, SolrQueryDao {

  @Inject
  protected ServerFactory serverFactory;
  protected final transient Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public boolean add(MultivalueMap<String, Object>... values) {
    boolean success = true;
    final SolrServer solrServer = serverFactory.getSolrServer();
    for (MultivalueMap<String, Object> val : values) {
      SolrInputDocument inputDocument = new SolrInputDocument();
      for (String key : val.keySet()) {
        List<Object> objects = val.get(key);
        for (Object object : objects) {
          inputDocument.addField(key, object);
        }
      }
      try {
        UpdateResponse response = solrServer.add(inputDocument);
        success = response.getStatus() == 0 && success;
      }
      catch (Exception ex) {
        logger.error("Could not add to solr index", ex);
        success = false;
      }
    }
    return success;
  }

  @Override
  public boolean deleteByQuery(String... queries) {
    boolean success = true;
    if (queries == null || queries.length <= 0) {
      return success;
    }
    final SolrServer solrServer = serverFactory.getSolrServer();
    for (String query : queries) {
      try {
        UpdateResponse response = solrServer.deleteByQuery(query);
        success = response.getStatus() == 0 && success;
      }
      catch (Exception ex) {
        logger.error("Could not delete from solr index", ex);
        success = false;
      }
    }
    return success;
  }

  @Override
  public boolean commit() {
    boolean success = true;
    try {
      UpdateResponse response = serverFactory.getSolrServer().commit();
      success = response.getStatus() == 0 && success;
    }
    catch (Exception ex) {
      logger.error("Could not commit to solr index", ex);
      success = false;
    }
    return success;
  }

  @Override
  public boolean rollback() {
    boolean success = true;
    try {
      UpdateResponse response = serverFactory.getSolrServer().rollback();
      success = response.getStatus() == 0 && success;
    }
    catch (Exception ex) {
      logger.error("Could not rollback from solr index", ex);
      success = false;
    }
    return success;
  }

  @Override
  public SearchResult<MultivalueMap<String, Object>> getResult(SolrParams query) {
    boolean success = true;
    List<MultivalueMap<String, Object>> list = new ArrayList<MultivalueMap<String, Object>>();
    SearchResult<MultivalueMap<String, Object>> result = null;
    try {
      final SolrServer solrServer = serverFactory.getSolrServer();
      QueryResponse response = solrServer.query(query);
      success = response.getStatus() == 0 && success;
      if (success) {
        SolrDocumentList documentList = response.getResults();
        if (logger.isInfoEnabled()) {
          logger.info("Solr Document List " + documentList);
        }
        if (documentList.getMaxScore() == null) {
          result = new SearchResult<MultivalueMap<String, Object>>(list, documentList.getNumFound());
        }
        else {
          result = new SearchResult<MultivalueMap<String, Object>>(list, documentList.getNumFound(), documentList.
              getMaxScore());
        }
        for (SolrDocument document : documentList) {
          MultivalueMap<String, Object> map = new MultivalueMapImpl<String, Object>();
          list.add(map);
          Map<String, Collection<Object>> values = document.getFieldValuesMap();
          for (String key : values.keySet()) {
            Collection<Object> oValues = values.get(key);
            for (Object value : oValues) {
              map.addValue(key, value);
            }
          }
        }
      }
    }
    catch (Exception ex) {
      logger.error("Could not search from solr index", ex);
      success = false;
    }
    if (result == null) {
      result = new SearchResult<MultivalueMap<String, Object>>(list);
    }
    return result;
  }
}
