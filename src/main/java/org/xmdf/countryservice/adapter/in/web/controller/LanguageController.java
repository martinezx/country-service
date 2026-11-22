package org.xmdf.countryservice.adapter.in.web.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.xmdf.countryservice.adapter.in.web.assembler.LanguageModelAssembler;
import org.xmdf.countryservice.adapter.in.web.assembler.PagedModels;
import org.xmdf.countryservice.adapter.in.web.model.CountryResponse;
import org.xmdf.countryservice.adapter.in.web.model.LanguageResponse;
import org.xmdf.countryservice.adapter.in.web.openapi.ApiTags;
import org.xmdf.countryservice.adapter.in.web.openapi.ItemResponses;
import org.xmdf.countryservice.adapter.in.web.openapi.PageResponses;
import org.xmdf.countryservice.adapter.in.web.paging.PageParameters;
import org.xmdf.countryservice.application.port.in.CountryQueryUseCase;
import org.xmdf.countryservice.application.port.in.LanguageQueryUseCase;

@RestController
@Tag(name = ApiTags.LANGUAGES)
@RequestMapping(
    value = "/languages",
    produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class LanguageController {

  private final LanguageQueryUseCase languages;
  private final CountryQueryUseCase countries;
  private final LanguageModelAssembler languageAssembler;
  private final CountryModelAssembler countryAssembler;

  public LanguageController(
      LanguageQueryUseCase languages,
      CountryQueryUseCase countries,
      LanguageModelAssembler languageAssembler,
      CountryModelAssembler countryAssembler) {
    this.languages = languages;
    this.countries = countries;
    this.languageAssembler = languageAssembler;
    this.countryAssembler = countryAssembler;
  }

  @Operation(operationId = "listLanguages", summary = "List languages")
  @GetMapping
  @PageResponses
  public PagedModel<EntityModel<LanguageResponse>> list(
      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        languages.findAll(PageParameters.toQuery(page, size)),
        languageAssembler::toModel,
        (p, s) -> linkTo(methodOn(LanguageController.class).list(p, s)).withSelfRel());
  }

  @Operation(operationId = "getLanguage", summary = "Get a language")
  @GetMapping("/{id}")
  @ItemResponses
  public EntityModel<LanguageResponse> get(@PathVariable Long id) {
    return languageAssembler.toModel(languages.getById(id));
  }

  @Operation(
      operationId = "listLanguageCountries",
      summary = "List the countries where a language is spoken")
  @GetMapping("/{id}/countries")
  @ItemResponses
  public PagedModel<EntityModel<CountryResponse>> countries(
      @PathVariable Long id,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        countries.findByLanguage(id, PageParameters.toQuery(page, size)),
        countryAssembler::toModel,
        (p, s) -> linkTo(methodOn(LanguageController.class).countries(id, p, s)).withSelfRel());
  }
}
