package org.xmdf.countryservice.adapter.in.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.adapter.in.web.controller.CountryController;
import org.xmdf.countryservice.adapter.in.web.controller.RegionController;
import org.xmdf.countryservice.adapter.in.web.model.CountryResponse;
import org.xmdf.countryservice.domain.Country;

@Component
public class CountryModelAssembler
    implements RepresentationModelAssembler<Country, EntityModel<CountryResponse>> {

  private final WebMapper mapper;

  CountryModelAssembler(WebMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public EntityModel<CountryResponse> toModel(Country country) {
    return EntityModel.of(
        mapper.toResponse(country),
        linkTo(methodOn(CountryController.class).get(country.id())).withSelfRel(),
        linkTo(methodOn(RegionController.class).get(country.region().id()))
            .withRel(LinkRels.REGION),
        linkTo(methodOn(CountryController.class).currencies(country.id(), null, null))
            .withRel(LinkRels.CURRENCIES),
        linkTo(methodOn(CountryController.class).languages(country.id(), null, null))
            .withRel(LinkRels.LANGUAGES));
  }
}
