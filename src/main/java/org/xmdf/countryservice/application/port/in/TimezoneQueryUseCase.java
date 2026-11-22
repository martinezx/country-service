package org.xmdf.countryservice.application.port.in;

import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.domain.Timezone;

/** Use case that reads the stored timezones. */
public interface TimezoneQueryUseCase {

  /**
   * Returns a page of timezones.
   *
   * @param query the page to return
   * @return the page, with the timezones ordered by their identifier
   */
  PagedResult<Timezone> findAll(PageQuery query);

  /**
   * Returns a timezone.
   *
   * @param id the identifier of the timezone
   * @return the timezone
   * @throws ResourceNotFoundException if there is no timezone with that identifier
   */
  Timezone getById(Long id);

  /**
   * Returns a page of the timezones of a country.
   *
   * @param countryId the identifier of the country
   * @param query the page to return
   * @return the page, with the timezones ordered by their identifier
   * @throws ResourceNotFoundException if there is no country with that identifier
   */
  PagedResult<Timezone> findByCountry(Long countryId, PageQuery query);
}
