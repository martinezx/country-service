package org.xmdf.countryservice.adapter.in.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.adapter.in.web.controller.RegionController;
import org.xmdf.countryservice.adapter.in.web.model.RegionResponse;
import org.xmdf.countryservice.domain.Region;

@Component
public class RegionModelAssembler
    implements RepresentationModelAssembler<Region, EntityModel<RegionResponse>> {

  private final WebMapper mapper;

  RegionModelAssembler(WebMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public EntityModel<RegionResponse> toModel(Region region) {
    return EntityModel.of(
        mapper.toResponse(region),
        linkTo(methodOn(RegionController.class).get(region.id())).withSelfRel(),
        linkTo(methodOn(RegionController.class).countries(region.id(), null, null))
            .withRel(LinkRels.COUNTRIES));
  }
}
