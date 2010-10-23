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
import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.ServerFactory;
import com.smartitengineering.dao.solr.SolrQueryDao;
import com.smartitengineering.dao.solr.SolrWriteDao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
 *
 * @author imyousuf
 */
public class SolrDao implements SolrWriteDao, SolrQueryDao {

  @Inject
  private ServerFactory serverFactory;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public boolean add(MultivalueMap<String, Object> values) {
    SolrInputDocument inputDocument = new SolrInputDocument();
    for (String key : values.keySet()) {
      List<Object> objects = values.get(key);
      for (Object object : objects) {
        inputDocument.addField(key, object);
      }
    }
    boolean success = true;
    try {
      final SolrServer solrServer = serverFactory.getSolrServer();
      UpdateResponse response = solrServer.add(inputDocument);
      success = response.getStatus() == 0 && success;
      response = solrServer.commit();
      success = response.getStatus() == 0 && success;
    }
    catch (Exception ex) {
      logger.error("Could not add to solr index", ex);
      success = false;
    }
    return success;
  }

  @Override
  public boolean deleteByQuery(String query) {
    boolean success = true;
    try {
      final SolrServer solrServer = serverFactory.getSolrServer();
      UpdateResponse response = solrServer.deleteByQuery(query);
      success = response.getStatus() == 0 && success;
    }
    catch (Exception ex) {
      logger.error("Could not delete from solr index", ex);
      success = false;
    }
    return success;
  }

  @Override
  public List<MultivalueMap<String, Object>> getResult(SolrParams query) {
    boolean success = true;
    List<MultivalueMap<String, Object>> list = new ArrayList<MultivalueMap<String, Object>>();
    try {
      final SolrServer solrServer = serverFactory.getSolrServer();
      QueryResponse response = solrServer.query(query);
      success = response.getStatus() == 0 && success;
      if (success) {
        SolrDocumentList documentList = response.getResults();
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
    return list;
  }
}
