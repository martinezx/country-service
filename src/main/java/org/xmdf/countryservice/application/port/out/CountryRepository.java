package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
import java.util.Optional;
import org.xmdf.countryservice.application.exception.InvalidRecordException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.domain.Country;

/** Storage for {@link Country countries}, including their currency and language links. */
public interface CountryRepository {

  /**
   * Stores the given countries together with their currency and language links. A country is
   * matched to an existing one by alpha-2 code: matches are updated and the others are inserted.
   * Stored countries that are not in the collection are left untouched. If the collection contains
   * several countries with the same alpha-2 code, the last one wins.
   *
   * <p>The region, the currencies and the timezones of each country must already be stored.
   * Languages that are not stored yet are created from the country's data, because the languages
   * offered by the source do not include every language that countries reference. Existing
   * languages are not modified. Links are only added: a link that is missing from a country is not
   * removed.
   *
   * @param countries the countries to store
   * @throws InvalidRecordException if the region, a currency or a timezone of a country is not
   *     stored. No country is stored partially: a country is stored with all its links or not at
   *     all.
   */
  void upsertAll(Collection<Country> countries);

  /**
   * Finds a page of countries.
   *
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code
   */
  PagedResult<Country> findAll(PageQuery query);

  /**
   * Finds a country by its identifier.
   *
   * @param id the identifier of the country
   * @return the country, or empty if there is none with that identifier
   */
  Optional<Country> findById(Long id);

  /**
   * Tells whether a country with the given identifier exists.
   *
   * @param id the identifier of the country
   * @return true if the country exists
   */
  boolean existsById(Long id);

  /**
   * Finds a page of the countries in a region.
   *
   * @param regionId the identifier of the region
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code. It is empty if the region does
   *     not exist or has no countries.
   */
  PagedResult<Country> findByRegionId(Long regionId, PageQuery query);

  /**
   * Finds a page of the countries that use a timezone.
   *
   * @param timezoneId the identifier of the timezone
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code. It is empty if the timezone does
   *     not exist or is used by no country.
   */
  PagedResult<Country> findByTimezoneId(Long timezoneId, PageQuery query);

  /**
   * Finds a page of the countries that use a currency.
   *
   * @param currencyId the identifier of the currency
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code. It is empty if the currency does
   *     not exist or is used by no country.
   */
  PagedResult<Country> findByCurrencyId(Long currencyId, PageQuery query);

  /**
   * Finds a page of the countries where a language is spoken.
   *
   * @param languageId the identifier of the language
   * @param query the page to return
   * @return the page, with the countries ordered by alpha-2 code. It is empty if the language does
   *     not exist or is spoken in no country.
   */
  PagedResult<Country> findByLanguageId(Long languageId, PageQuery query);
}
