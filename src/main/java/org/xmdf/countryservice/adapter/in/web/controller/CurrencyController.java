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
import org.xmdf.countryservice.adapter.in.web.assembler.PagedModels;
import org.xmdf.countryservice.adapter.in.web.model.CountryResponse;
import org.xmdf.countryservice.adapter.in.web.model.CurrencyResponse;
import org.xmdf.countryservice.adapter.in.web.openapi.ApiTags;
import org.xmdf.countryservice.adapter.in.web.openapi.ItemResponses;
import org.xmdf.countryservice.adapter.in.web.openapi.PageResponses;
import org.xmdf.countryservice.adapter.in.web.paging.PageParameters;
import org.xmdf.countryservice.application.port.in.CountryQueryUseCase;
import org.xmdf.countryservice.application.port.in.CurrencyQueryUseCase;

@RestController
@Tag(name = ApiTags.CURRENCIES)
@RequestMapping(
    value = "/currencies",
    produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class CurrencyController {

  private final CurrencyQueryUseCase currencies;
  private final CountryQueryUseCase countries;
  private final CurrencyModelAssembler currencyAssembler;
  private final CountryModelAssembler countryAssembler;

  public CurrencyController(
      CurrencyQueryUseCase currencies,
      CountryQueryUseCase countries,
      CurrencyModelAssembler currencyAssembler,
      CountryModelAssembler countryAssembler) {
    this.currencies = currencies;
    this.countries = countries;
    this.currencyAssembler = currencyAssembler;
    this.countryAssembler = countryAssembler;
  }

  @Operation(operationId = "listCurrencies", summary = "List currencies")
  @GetMapping
  @PageResponses
  public PagedModel<EntityModel<CurrencyResponse>> list(
      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        currencies.findAll(PageParameters.toQuery(page, size)),
        currencyAssembler::toModel,
        (p, s) -> linkTo(methodOn(CurrencyController.class).list(p, s)).withSelfRel());
  }

  @Operation(operationId = "getCurrency", summary = "Get a currency")
  @GetMapping("/{id}")
  @ItemResponses
  public EntityModel<CurrencyResponse> get(@PathVariable Long id) {
    return currencyAssembler.toModel(currencies.getById(id));
  }

  @Operation(
      operationId = "listCurrencyCountries",
      summary = "List the countries that use a currency")
  @GetMapping("/{id}/countries")
  @ItemResponses
  public PagedModel<EntityModel<CountryResponse>> countries(
      @PathVariable Long id,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return PagedModels.of(
        countries.findByCurrency(id, PageParameters.toQuery(page, size)),
        countryAssembler::toModel,
        (p, s) -> linkTo(methodOn(CurrencyController.class).countries(id, p, s)).withSelfRel());
  }
}
