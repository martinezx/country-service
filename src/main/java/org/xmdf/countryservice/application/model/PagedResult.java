package org.xmdf.countryservice.application.model;

import java.util.List;

/**
 * One page of a list.
 *
 * @param content the elements of the page, in order
 * @param page the zero-based index of the page
 * @param size the maximum number of elements the page can hold
 * @param totalElements the number of elements in the whole list
 * @param <T> the type of the elements
 */
public record PagedResult<T>(List<T> content, int page, int size, long totalElements) {

  /**
   * Returns the number of pages the whole list is divided into.
   *
   * @return the number of pages, 0 if the list is empty
   */
  public int totalPages() {
    return (int) ((totalElements + size - 1) / size);
  }
}
