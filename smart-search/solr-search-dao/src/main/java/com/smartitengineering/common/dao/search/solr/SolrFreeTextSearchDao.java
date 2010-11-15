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
package com.smartitengineering.common.dao.search.solr;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.smartitengineering.common.dao.search.CommonFreeTextSearchDao;
import com.smartitengineering.common.dao.search.SearchResult;
import com.smartitengineering.dao.common.queryparam.Order;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterCastHelper;
import com.smartitengineering.dao.common.queryparam.SimpleNameValueQueryParameter;
import com.smartitengineering.dao.common.queryparam.StringLikeQueryParameter;
import com.smartitengineering.dao.common.queryparam.ValueOnlyQueryParameter;
import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.SolrQueryDao;
import com.smartitengineering.util.bean.adapter.GenericAdapter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

/**
 *
 * @author imyousuf
 */
public class SolrFreeTextSearchDao<T> implements CommonFreeTextSearchDao<T> {

  @Inject
  private GenericAdapter<T, MultivalueMap<String, Object>> adapter;
  @Inject
  private SolrQueryDao queryDao;
  @Inject
  private ExecutorService executorService;
  @Inject
  @Named("waitTime")
  private long waitTime;
  @Inject
  @Named("waitTimeUnit")
  private TimeUnit waitTimeUnit;

  @Override
  public SearchResult<T> detailedSearch(List<QueryParameter> parameters) {
    SolrQuery query = new SolrQuery();
    for (QueryParameter param : parameters) {
      switch (param.getParameterType()) {
        case PARAMETER_TYPE_PROPERTY:
          StringLikeQueryParameter queryParameter =
                                   QueryParameterCastHelper.STRING_PARAM_HELPER.cast(param);
          if (queryParameter.getPropertyName().equals("q")) {
            String queryString = queryParameter.getValue();
            query.setQuery(queryString);
          }
          else {
            query.add(queryParameter.getPropertyName(), queryParameter.getValue());
          }
          break;
        case PARAMETER_TYPE_ORDER_BY:
          SimpleNameValueQueryParameter<Order> parameter = QueryParameterCastHelper.SIMPLE_PARAM_HELPER.cast(param);
          ORDER order = parameter.getValue().equals(Order.ASC) ? ORDER.asc : ORDER.desc;
          query.setSortField(parameter.getPropertyName(), order);
          break;
        case PARAMETER_TYPE_MAX_RESULT:
          ValueOnlyQueryParameter<Integer> valParam = QueryParameterCastHelper.VALUE_PARAM_HELPER.cast(param);
          Integer maxRows = valParam.getValue();
          query.setRows(maxRows);
          break;
        case PARAMETER_TYPE_FIRST_RESULT:
          ValueOnlyQueryParameter<Integer> firstParam = QueryParameterCastHelper.VALUE_PARAM_HELPER.cast(param);
          Integer firstResult = firstParam.getValue();
          query.setStart(firstResult);
          break;
        default:
          throw new UnsupportedOperationException("Only property and order by query parameter is supported!");
      }
    }
    query.setIncludeScore(true);
    final SearchResult<MultivalueMap<String, Object>> mainResult = queryDao.getResult(query);
    final Collection<MultivalueMap<String, Object>> result = mainResult.getResult();
    final SearchResult<T> actualResult = new SearchResult<T>(adapter.convertInversely(result.toArray(new MultivalueMap[result.
        size()])), mainResult);
    return actualResult;
  }

  @Override
  public SearchResult<T> detailedSearch(QueryParameter... parameters) {
    return detailedSearch(Arrays.asList(parameters));
  }

  @Override
  public Collection<T> search(QueryParameter... parameters) {
    return detailedSearch(Arrays.asList(parameters)).getResult();
  }

  @Override
  public Collection<T> search(List<QueryParameter> parameters) {
    return detailedSearch(parameters).getResult();
  }

  public GenericAdapter<T, MultivalueMap<String, Object>> getAdapter() {
    return adapter;
  }

  public void setAdapter(GenericAdapter<T, MultivalueMap<String, Object>> adapter) {
    this.adapter = adapter;
  }

  public ExecutorService getExecutorService() {
    return executorService;
  }

  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  public SolrQueryDao getQueryDao() {
    return queryDao;
  }

  public void setQueryDao(SolrQueryDao queryDao) {
    this.queryDao = queryDao;
  }

  public long getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(long waitTime) {
    this.waitTime = waitTime;
  }

  public TimeUnit getWaitTimeUnit() {
    return waitTimeUnit;
  }

  public void setWaitTimeUnit(TimeUnit waitTimeUnit) {
    this.waitTimeUnit = waitTimeUnit;
  }
}
