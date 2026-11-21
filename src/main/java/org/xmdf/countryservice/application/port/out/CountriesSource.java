package org.xmdf.countryservice.application.port.out;

import java.util.List;
import org.xmdf.countryservice.domain.Country;
import org.xmdf.countryservice.domain.Currency;
import org.xmdf.countryservice.domain.Language;
import org.xmdf.countryservice.domain.Region;
import org.xmdf.countryservice.domain.Timezone;

/**
 * Source of the country data, which is countries.dev. Implementations signal a failure to reach or
 * read the source with an unchecked exception.
 *
 * <p>Regions and timezones are small and are returned in full by a single call. Currencies,
 * languages and countries are paged: the caller requests pages of increasing offset until an empty
 * page is returned.
 */
public interface CountriesSource {

  /**
   * Fetches all regions.
   *
   * @return the regions, never null
   */
  List<Region> fetchRegions();

  /**
   * Fetches all timezones.
   *
   * @return the timezones, never null
   */
  List<Timezone> fetchTimezones();

  /**
   * Fetches a page of currencies, ordered by currency code.
   *
   * @param limit the maximum number of currencies to return
   * @param offset the number of currencies to skip
   * @return the currencies of the requested page, empty when the offset is past the last currency
   */
  List<Currency> fetchCurrencies(int limit, int offset);

  /**
   * Fetches a page of languages, ordered by ISO 639-1 code.
   *
   * @param limit the maximum number of languages to return
   * @param offset the number of languages to skip
   * @return the languages of the requested page, empty when the offset is past the last language
   */
  List<Language> fetchLanguages(int limit, int offset);

  /**
   * Fetches a page of countries, ordered by numeric code. Each country carries its region,
   * currencies and languages.
   *
   * @param limit the maximum number of countries to return
   * @param offset the number of countries to skip
   * @return the countries of the requested page, empty when the offset is past the last country
   */
  List<Country> fetchCountries(int limit, int offset);
}
