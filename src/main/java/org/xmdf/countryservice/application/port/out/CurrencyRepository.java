package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
import java.util.Optional;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.domain.Currency;

/** Storage for {@link Currency currencies}. */
public interface CurrencyRepository {

  /**
   * Stores the given currencies. A currency is matched to an existing one by code: matches are
   * updated and the others are inserted. Stored currencies that are not in the collection are left
   * untouched. If the collection contains several currencies with the same code, the last one wins.
   *
   * @param currencies the currencies to store
   */
  void upsertAll(Collection<Currency> currencies);

  /**
   * Finds a page of currencies.
   *
   * @param query the page to return
   * @return the page, with the currencies ordered by code
   */
  PagedResult<Currency> findAll(PageQuery query);

  /**
   * Finds a currency by its identifier.
   *
   * @param id the identifier of the currency
   * @return the currency, or empty if there is none with that identifier
   */
  Optional<Currency> findById(Long id);

  /**
   * Tells whether a currency with the given identifier exists.
   *
   * @param id the identifier of the currency
   * @return true if the currency exists
   */
  boolean existsById(Long id);

  /**
   * Finds a page of the currencies used in a country.
   *
   * @param countryId the identifier of the country
   * @param query the page to return
   * @return the page, with the currencies ordered by code. It is empty if the country does not
   *     exist or uses no currencies.
   */
  PagedResult<Currency> findByCountryId(Long countryId, PageQuery query);
}
