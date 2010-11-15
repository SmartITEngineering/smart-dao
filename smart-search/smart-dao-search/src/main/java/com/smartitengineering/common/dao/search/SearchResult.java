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
package com.smartitengineering.common.dao.search;

import java.util.Collection;

/**
 *
 * @author imyousuf
 */
public class SearchResult<T> {

  private final Collection<T> result;
  private final long totalResults;
  private final double maxScore;
  private final boolean totalResultsSupported;
  private final boolean maxScoreSupported;

  public SearchResult(Collection<T> result) {
    this(result, 0, 0.0, false, false);
  }

  public SearchResult(Collection<T> result, SearchResult withOtherInfo) {
    this(result, withOtherInfo.getTotalResults(), withOtherInfo.getMaxScore(), withOtherInfo.isTotalResultsSupported(),
         withOtherInfo.isMaxScoreSupported());
  }

  public SearchResult(Collection<T> result, long totalResults) {
    this(result, totalResults, 0.0, true, false);
  }

  public SearchResult(Collection<T> result, double maxScore) {
    this(result, 0, maxScore, false, true);
  }

  public SearchResult(Collection<T> result, long totalResults, double maxScore) {
    this(result, totalResults, maxScore, true, true);
  }

  protected SearchResult(Collection<T> result, long totalResults, double maxScore, boolean totalResultsSupported,
                         boolean maxScoreSupported) {
    this.result = result;
    this.totalResults = totalResults;
    this.maxScore = maxScore;
    this.totalResultsSupported = totalResultsSupported;
    this.maxScoreSupported = maxScoreSupported;
  }

  public Collection<T> getResult() {
    return result;
  }

  public double getMaxScore() {
    return maxScore;
  }

  public boolean isMaxScoreSupported() {
    return maxScoreSupported;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public boolean isTotalResultsSupported() {
    return totalResultsSupported;
  }
}
