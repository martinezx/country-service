package org.xmdf.countryservice.application.port.in;

import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.domain.Country;

/** Use case that reads the stored countries. */
public interface CountryQueryUseCase {

  /**
   * Returns a page of countries.
   *
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code
   */
  PagedResult<Country> findAll(PageQuery query);

  /**
   * Returns a country.
   *
   * @param id the identifier of the country
   * @return the country
   * @throws ResourceNotFoundException if there is no country with that identifier
   */
  Country getById(Long id);

  /**
   * Returns a page of the countries in a region.
   *
   * @param regionId the identifier of the region
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code
   * @throws ResourceNotFoundException if there is no region with that identifier
   */
  PagedResult<Country> findByRegion(Long regionId, PageQuery query);

  /**
   * Returns a page of the countries that use a timezone.
   *
   * @param timezoneId the identifier of the timezone
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code
   * @throws ResourceNotFoundException if there is no timezone with that identifier
   */
  PagedResult<Country> findByTimezone(Long timezoneId, PageQuery query);

  /**
   * Returns a page of the countries that use a currency.
   *
   * @param currencyId the identifier of the currency
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code
   * @throws ResourceNotFoundException if there is no currency with that identifier
   */
  PagedResult<Country> findByCurrency(Long currencyId, PageQuery query);

  /**
   * Returns a page of the countries where a language is spoken.
   *
   * @param languageId the identifier of the language
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code
   * @throws ResourceNotFoundException if there is no language with that identifier
   */
  PagedResult<Country> findByLanguage(Long languageId, PageQuery query);
}
