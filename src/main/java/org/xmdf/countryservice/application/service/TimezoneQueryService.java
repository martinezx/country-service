package org.xmdf.countryservice.application.service;

import org.springframework.stereotype.Service;
import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.in.TimezoneQueryUseCase;
import org.xmdf.countryservice.application.port.out.CountryRepository;
import org.xmdf.countryservice.application.port.out.TimezoneRepository;
import org.xmdf.countryservice.domain.Timezone;

@Service
public class TimezoneQueryService implements TimezoneQueryUseCase {

  private final TimezoneRepository timezones;
  private final CountryRepository countries;

  public TimezoneQueryService(TimezoneRepository timezones, CountryRepository countries) {
    this.timezones = timezones;
    this.countries = countries;
  }

  @Override
  public PagedResult<Timezone> findAll(PageQuery query) {
    return timezones.findAll(query);
  }

  @Override
  public Timezone getById(Long id) {
    return timezones.findById(id).orElseThrow(() -> new ResourceNotFoundException("Timezone", id));
  }

  @Override
  public PagedResult<Timezone> findByCountry(Long countryId, PageQuery query) {
    if (!countries.existsById(countryId)) {
      throw new ResourceNotFoundException("Country", countryId);
    }
    return timezones.findByCountryId(countryId, query);
  }
}
