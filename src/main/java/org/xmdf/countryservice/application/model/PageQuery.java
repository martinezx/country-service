package org.xmdf.countryservice.application.model;

/**
 * A request for one page of a list.
 *
 * @param page the zero-based index of the page
 * @param size the maximum number of elements in the page
 */
public record PageQuery(int page, int size) {

  public PageQuery {
    if (page < 0) {
      throw new IllegalArgumentException("page must not be negative");
    }
    if (size < 1) {
      throw new IllegalArgumentException("size must be at least 1");
    }
  }
}
