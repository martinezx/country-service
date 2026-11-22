package org.xmdf.countryservice.application.service;

import org.springframework.stereotype.Service;
import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.in.TimezoneQueryUseCase;
import org.xmdf.countryservice.application.port.out.TimezoneRepository;
import org.xmdf.countryservice.domain.Timezone;

@Service
public class TimezoneQueryService implements TimezoneQueryUseCase {

  private final TimezoneRepository timezones;

  public TimezoneQueryService(TimezoneRepository timezones) {
    this.timezones = timezones;
  }

  @Override
  public PagedResult<Timezone> findAll(PageQuery query) {
    return timezones.findAll(query);
  }

  @Override
  public Timezone getById(Long id) {
    return timezones.findById(id).orElseThrow(() -> new ResourceNotFoundException("Timezone", id));
  }
}
