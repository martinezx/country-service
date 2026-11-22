package org.xmdf.countryservice.adapter.out.persistence;

import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.xmdf.countryservice.application.model.PagedResult;

final class PagedResults {

  private PagedResults() {}

  static <E, D> PagedResult<D> of(Page<E> page, Function<E, D> mapper) {
    return new PagedResult<>(
        page.getContent().stream().map(mapper).toList(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements());
  }
}
