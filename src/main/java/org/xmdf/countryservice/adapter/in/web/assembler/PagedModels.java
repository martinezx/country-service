package org.xmdf.countryservice.adapter.in.web.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.xmdf.countryservice.application.model.PagedResult;

public final class PagedModels {

  private PagedModels() {}

  public static <D, R> PagedModel<EntityModel<R>> of(
      PagedResult<D> result,
      Function<D, EntityModel<R>> assembler,
      BiFunction<Integer, Integer, Link> pageLink) {
    int page = result.page();
    int size = result.size();
    int totalPages = result.totalPages();

    List<Link> links = new ArrayList<>();
    links.add(pageLink.apply(page, size).withSelfRel());
    links.add(pageLink.apply(0, size).withRel(IanaLinkRelations.FIRST));
    if (page > 0) {
      links.add(pageLink.apply(page - 1, size).withRel(IanaLinkRelations.PREV));
    }
    if (page + 1 < totalPages) {
      links.add(pageLink.apply(page + 1, size).withRel(IanaLinkRelations.NEXT));
    }
    links.add(pageLink.apply(Math.max(totalPages - 1, 0), size).withRel(IanaLinkRelations.LAST));

    List<EntityModel<R>> content = result.content().stream().map(assembler).toList();
    return PagedModel.of(
        content, new PageMetadata(size, page, result.totalElements(), totalPages), links);
  }
}
