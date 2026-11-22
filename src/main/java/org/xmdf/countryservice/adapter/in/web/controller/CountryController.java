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
import org.xmdf.countryservice.adapter.in.web.assembler.CurrencyModelAssembler;
import org.xmdf.countryservice.adapter.in.web.assembler.LanguageModelAssembler;
import org.xmdf.countryservice.adapter.in.web.assembler.PagedModels;
import org.xmdf.countryservice.adapter.in.web.model.CountryResponse;
import org.xmdf.countryservice.adapter.in.web.model.CurrencyResponse;
import org.xmdf.countryservice.adapter.in.web.model.LanguageResponse;
import org.xmdf.countryservice.adapter.in.web.openapi.ApiTags;
import org.xmdf.countryservice.adapter.in.web.openapi.ItemResponses;
import org.xmdf.countryservice.adapter.in.web.openapi.PageResponses;
import org.xmdf.countryservice.adapter.in.web.paging.PageParameters;
import org.xmdf.countryservice.application.port.in.CountryQueryUseCase;
import org.xmdf.countryservice.application.port.in.CurrencyQueryUseCase;
import org.xmdf.countryservice.application.port.in.LanguageQueryUseCase;

@RestController
@Tag(name = ApiTags.COUNTRIES)
@RequestMapping(
    value = "/countries",
    produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class CountryController {

  private final CountryQueryUseCase countries;
  private final CurrencyQueryUseCase currencies;
  private final LanguageQueryUseCase languages;
  private final CountryModelAssembler countryAssembler;
  private final CurrencyModelAssembler currencyAssembler;
  private final LanguageModelAssembler languageAssembler;

  public CountryController(
      CountryQueryUseCase countries,
      CurrencyQueryUseCase currencies,
      LanguageQueryUseCase languages,
      CountryModelAssembler countryAssembler,
      CurrencyModelAssembler currencyAssembler,
      LanguageModelAssembler languageAssembler) {
    this.countries = countries;
    this.currencies = currencies;
    this.languages = languages;
    this.countryAssembler = countryAssembler;
    this.currencyAssembler = currencyAssembler;
    this.languageAssembler = languageAssembler;
  }

  @Operation(operationId = "listCountries", summary = "List countries")
  @GetMapping
  @PageResponses
  public PagedModel<EntityModel<CountryResponse>> list(
      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        countries.findAll(PageParameters.toQuery(page, size)),
        countryAssembler::toModel,
        (p, s) -> linkTo(methodOn(CountryController.class).list(p, s)).withSelfRel());
  }

  @Operation(operationId = "getCountry", summary = "Get a country")
  @GetMapping("/{id}")
  @ItemResponses
  public EntityModel<CountryResponse> get(@PathVariable Long id) {
    return countryAssembler.toModel(countries.getById(id));
  }

  @Operation(
      operationId = "listCountryCurrencies",
      summary = "List the currencies used in a country")
  @GetMapping("/{id}/currencies")
  @ItemResponses
  public PagedModel<EntityModel<CurrencyResponse>> currencies(
      @PathVariable Long id,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        currencies.findByCountry(id, PageParameters.toQuery(page, size)),
        currencyAssembler::toModel,
        (p, s) -> linkTo(methodOn(CountryController.class).currencies(id, p, s)).withSelfRel());
  }

  @Operation(
      operationId = "listCountryLanguages",
      summary = "List the languages spoken in a country")
  @GetMapping("/{id}/languages")
  @ItemResponses
  public PagedModel<EntityModel<LanguageResponse>> languages(
      @PathVariable Long id,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        languages.findByCountry(id, PageParameters.toQuery(page, size)),
        languageAssembler::toModel,
        (p, s) -> linkTo(methodOn(CountryController.class).languages(id, p, s)).withSelfRel());
  }
}
