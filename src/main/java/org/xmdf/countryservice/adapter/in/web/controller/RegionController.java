package org.xmdf.countryservice.adapter.in.web.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
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
import org.xmdf.countryservice.adapter.in.web.assembler.RegionModelAssembler;
import org.xmdf.countryservice.adapter.in.web.model.CountryResponse;
import org.xmdf.countryservice.adapter.in.web.model.RegionResponse;
import org.xmdf.countryservice.adapter.in.web.openapi.ApiTags;
import org.xmdf.countryservice.adapter.in.web.openapi.ItemResponses;
import org.xmdf.countryservice.adapter.in.web.paging.PageParameters;
import org.xmdf.countryservice.application.port.in.CountryQueryUseCase;
import org.xmdf.countryservice.application.port.in.RegionQueryUseCase;

@RestController
@Tag(name = ApiTags.REGIONS)
@RequestMapping(
    value = "/regions",
    produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class RegionController {

  private final RegionQueryUseCase regions;
  private final CountryQueryUseCase countries;
  private final RegionModelAssembler regionAssembler;
  private final CountryModelAssembler countryAssembler;

  public RegionController(
      RegionQueryUseCase regions,
      CountryQueryUseCase countries,
      RegionModelAssembler regionAssembler,
      CountryModelAssembler countryAssembler) {
    this.regions = regions;
    this.countries = countries;
    this.regionAssembler = regionAssembler;
    this.countryAssembler = countryAssembler;
  }

  @Operation(operationId = "listRegions", summary = "List regions")
  @GetMapping
  public CollectionModel<EntityModel<RegionResponse>> list() {
    return CollectionModel.of(
        regions.findAll().stream().map(regionAssembler::toModel).toList(),
        linkTo(methodOn(RegionController.class).list()).withSelfRel());
  }

  @Operation(operationId = "getRegion", summary = "Get a region")
  @GetMapping("/{id}")
  @ItemResponses
  public EntityModel<RegionResponse> get(@PathVariable Long id) {
    return regionAssembler.toModel(regions.getById(id));
  }

  @Operation(operationId = "listRegionCountries", summary = "List the countries in a region")
  @GetMapping("/{id}/countries")
  @ItemResponses
  public PagedModel<EntityModel<CountryResponse>> countries(
      @PathVariable Long id,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        countries.findByRegion(id, PageParameters.toQuery(page, size)),
        countryAssembler::toModel,
        (p, s) -> linkTo(methodOn(RegionController.class).countries(id, p, s)).withSelfRel());
  }
}
