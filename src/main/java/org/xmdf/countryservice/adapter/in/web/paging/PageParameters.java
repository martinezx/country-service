package org.xmdf.countryservice.adapter.in.web.paging;

import org.xmdf.countryservice.application.model.PageQuery;

public final class PageParameters {

  public static final int DEFAULT_SIZE = 20;
  public static final int MAX_SIZE = 100;

  private PageParameters() {}

  public static PageQuery toQuery(Integer page, Integer size) {
    int resolvedPage = page == null ? 0 : page;
    int resolvedSize = size == null ? DEFAULT_SIZE : size;
    if (resolvedPage < 0) {
      throw new InvalidPageRequestException("page must not be negative");
    }
    if (resolvedSize < 1 || resolvedSize > MAX_SIZE) {
      throw new InvalidPageRequestException("size must be between 1 and " + MAX_SIZE);
    }
    return new PageQuery(resolvedPage, resolvedSize);
  }
}
