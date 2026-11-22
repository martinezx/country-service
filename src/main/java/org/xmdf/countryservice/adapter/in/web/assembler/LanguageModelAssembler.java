package org.xmdf.countryservice.adapter.in.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.adapter.in.web.controller.LanguageController;
import org.xmdf.countryservice.adapter.in.web.model.LanguageResponse;
import org.xmdf.countryservice.domain.Language;

@Component
public class LanguageModelAssembler
    implements RepresentationModelAssembler<Language, EntityModel<LanguageResponse>> {

  private final WebMapper mapper;

  LanguageModelAssembler(WebMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public EntityModel<LanguageResponse> toModel(Language language) {
    return EntityModel.of(
        mapper.toResponse(language),
        linkTo(methodOn(LanguageController.class).get(language.id())).withSelfRel(),
        linkTo(methodOn(LanguageController.class).countries(language.id(), null, null))
            .withRel(LinkRels.COUNTRIES));
  }
}
