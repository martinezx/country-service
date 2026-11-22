package org.xmdf.countryservice.application.service;

import org.springframework.stereotype.Service;
import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.in.CountryQueryUseCase;
import org.xmdf.countryservice.application.port.out.CountryRepository;
import org.xmdf.countryservice.application.port.out.CurrencyRepository;
import org.xmdf.countryservice.application.port.out.LanguageRepository;
import org.xmdf.countryservice.application.port.out.RegionRepository;
import org.xmdf.countryservice.application.port.out.TimezoneRepository;
import org.xmdf.countryservice.domain.Country;

@Service
public class CountryQueryService implements CountryQueryUseCase {

  private final CountryRepository countries;
  private final RegionRepository regions;
  private final CurrencyRepository currencies;
  private final LanguageRepository languages;
  private final TimezoneRepository timezones;

  public CountryQueryService(
      CountryRepository countries,
      RegionRepository regions,
      CurrencyRepository currencies,
      LanguageRepository languages,
      TimezoneRepository timezones) {
    this.countries = countries;
    this.regions = regions;
    this.currencies = currencies;
    this.languages = languages;
    this.timezones = timezones;
  }

  @Override
  public PagedResult<Country> findAll(PageQuery query) {
    return countries.findAll(query);
  }

  @Override
  public Country getById(Long id) {
    return countries.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country", id));
  }

  @Override
  public PagedResult<Country> findByRegion(Long regionId, PageQuery query) {
    if (!regions.existsById(regionId)) {
      throw new ResourceNotFoundException("Region", regionId);
    }
    return countries.findByRegionId(regionId, query);
  }

  @Override
  public PagedResult<Country> findByTimezone(Long timezoneId, PageQuery query) {
    if (!timezones.existsById(timezoneId)) {
      throw new ResourceNotFoundException("Timezone", timezoneId);
    }
    return countries.findByTimezoneId(timezoneId, query);
  }

  @Override
  public PagedResult<Country> findByCurrency(Long currencyId, PageQuery query) {
    if (!currencies.existsById(currencyId)) {
      throw new ResourceNotFoundException("Currency", currencyId);
    }
    return countries.findByCurrencyId(currencyId, query);
  }

  @Override
  public PagedResult<Country> findByLanguage(Long languageId, PageQuery query) {
    if (!languages.existsById(languageId)) {
      throw new ResourceNotFoundException("Language", languageId);
    }
    return countries.findByLanguageId(languageId, query);
  }
}
