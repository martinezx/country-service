package org.xmdf.countryservice.adapter.in.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.adapter.in.web.controller.TimezoneController;
import org.xmdf.countryservice.adapter.in.web.model.TimezoneResponse;
import org.xmdf.countryservice.domain.Timezone;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TimezoneModelAssembler
    implements RepresentationModelAssembler<Timezone, EntityModel<TimezoneResponse>> {

  private final WebMapper mapper;

  @Override
  public EntityModel<TimezoneResponse> toModel(Timezone timezone) {
    return EntityModel.of(
        mapper.toResponse(timezone),
        linkTo(methodOn(TimezoneController.class).get(timezone.id())).withSelfRel(),
        linkTo(methodOn(TimezoneController.class).countries(timezone.id(), null, null))
            .withRel(LinkRels.COUNTRIES));
  }
}
