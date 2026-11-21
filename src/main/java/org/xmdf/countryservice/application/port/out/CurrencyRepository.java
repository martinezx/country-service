package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
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
}
