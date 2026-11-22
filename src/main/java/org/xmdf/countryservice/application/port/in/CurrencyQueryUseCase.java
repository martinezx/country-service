package org.xmdf.countryservice.application.port.in;

import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.domain.Currency;

/** Use case that reads the stored currencies. */
public interface CurrencyQueryUseCase {

  /**
   * Returns a page of currencies.
   *
   * @param query the page to return
   * @return the page, with the currencies ordered by code
   */
  PagedResult<Currency> findAll(PageQuery query);

  /**
   * Returns a currency.
   *
   * @param id the identifier of the currency
   * @return the currency
   * @throws ResourceNotFoundException if there is no currency with that identifier
   */
  Currency getById(Long id);

  /**
   * Returns a page of the currencies used in a country.
   *
   * @param countryId the identifier of the country
   * @param query the page to return
   * @return the page, with the currencies ordered by code
   * @throws ResourceNotFoundException if there is no country with that identifier
   */
  PagedResult<Currency> findByCountry(Long countryId, PageQuery query);
}
