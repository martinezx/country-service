package org.xmdf.countryservice.adapter.in.web.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xmdf.countryservice.adapter.in.web.assembler.CountryModelAssembler;
import org.xmdf.countryservice.adapter.in.web.assembler.PagedModels;
import org.xmdf.countryservice.adapter.in.web.assembler.TimezoneModelAssembler;
import org.xmdf.countryservice.adapter.in.web.model.CountryResponse;
import org.xmdf.countryservice.adapter.in.web.model.TimezoneResponse;
import org.xmdf.countryservice.adapter.in.web.openapi.ApiTags;
import org.xmdf.countryservice.adapter.in.web.openapi.ItemResponses;
import org.xmdf.countryservice.adapter.in.web.openapi.PageResponses;
import org.xmdf.countryservice.adapter.in.web.paging.PageParameters;
import org.xmdf.countryservice.application.port.in.CountryQueryUseCase;
import org.xmdf.countryservice.application.port.in.TimezoneQueryUseCase;

@RestController
@Tag(name = ApiTags.TIMEZONES)
@RequestMapping(
    value = "/timezones",
    produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class TimezoneController {

  private final TimezoneQueryUseCase timezones;
  private final CountryQueryUseCase countries;
  private final TimezoneModelAssembler timezoneAssembler;
  private final CountryModelAssembler countryAssembler;

  @Operation(operationId = "listTimezones", summary = "List timezones")
  @GetMapping
  @PageResponses
  public PagedModel<EntityModel<TimezoneResponse>> list(
      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        timezones.findAll(PageParameters.toQuery(page, size)),
        timezoneAssembler::toModel,
        (p, s) -> linkTo(methodOn(TimezoneController.class).list(p, s)).withSelfRel());
  }

  @Operation(operationId = "getTimezone", summary = "Get a timezone")
  @GetMapping("/{id}")
  @ItemResponses
  public EntityModel<TimezoneResponse> get(@PathVariable Long id) {
    return timezoneAssembler.toModel(timezones.getById(id));
  }

  @Operation(
      operationId = "listTimezoneCountries",
      summary = "List the countries that use a timezone")
  @GetMapping("/{id}/countries")
  @ItemResponses
  public PagedModel<EntityModel<CountryResponse>> countries(
      @PathVariable Long id,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        countries.findByTimezone(id, PageParameters.toQuery(page, size)),
        countryAssembler::toModel,
        (p, s) -> linkTo(methodOn(TimezoneController.class).countries(id, p, s)).withSelfRel());
  }
}
