package org.xmdf.countryservice.adapter.in.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.adapter.in.web.controller.CurrencyController;
import org.xmdf.countryservice.adapter.in.web.model.CurrencyResponse;
import org.xmdf.countryservice.domain.Currency;

@Component
public class CurrencyModelAssembler
    implements RepresentationModelAssembler<Currency, EntityModel<CurrencyResponse>> {

  private final WebMapper mapper;

  CurrencyModelAssembler(WebMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public EntityModel<CurrencyResponse> toModel(Currency currency) {
    return EntityModel.of(
        mapper.toResponse(currency),
        linkTo(methodOn(CurrencyController.class).get(currency.id())).withSelfRel(),
        linkTo(methodOn(CurrencyController.class).countries(currency.id(), null, null))
            .withRel(LinkRels.COUNTRIES));
  }
}
