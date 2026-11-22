package org.xmdf.countryservice.adapter.in.web.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmdf.countryservice.adapter.in.web.assembler.LinkRels;
import org.xmdf.countryservice.adapter.in.web.openapi.ApiTags;

@RestController
@Tag(name = ApiTags.ROOT)
public class ApiRootController {

  @Operation(operationId = "getApiRoot", summary = "List the links to all collections of the API")
  @GetMapping(
      value = "/",
      produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public RepresentationModel<?> root() {
    RepresentationModel<?> root = new RepresentationModel<>();
    root.add(
        linkTo(methodOn(ApiRootController.class).root()).withSelfRel(),
        linkTo(methodOn(RegionController.class).list()).withRel(LinkRels.REGIONS),
        linkTo(methodOn(CountryController.class).list(null, null)).withRel(LinkRels.COUNTRIES),
        linkTo(methodOn(CurrencyController.class).list(null, null)).withRel(LinkRels.CURRENCIES),
        linkTo(methodOn(LanguageController.class).list(null, null)).withRel(LinkRels.LANGUAGES),
        linkTo(methodOn(TimezoneController.class).list(null, null)).withRel(LinkRels.TIMEZONES));
    return root;
  }
}
