package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
import org.xmdf.countryservice.domain.Country;

/** Storage for {@link Country countries}, including their currency and language links. */
public interface CountryRepository {

  /**
   * Stores the given countries together with their currency and language links. A country is
   * matched to an existing one by alpha-2 code: matches are updated and the others are inserted.
   * Stored countries that are not in the collection are left untouched. If the collection contains
   * several countries with the same alpha-2 code, the last one wins.
   *
   * <p>The region and the currencies of each country must already be stored. Languages that are not
   * stored yet are created from the country's data, because the languages offered by the source do
   * not include every language that countries reference. Existing languages are not modified. Links
   * are only added: a link that is missing from a country is not removed.
   *
   * @param countries the countries to store
   * @throws IllegalStateException if the region or a currency of a country is not stored
   */
  void upsertAll(Collection<Country> countries);
}
