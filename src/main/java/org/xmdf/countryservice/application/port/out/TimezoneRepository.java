package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
import org.xmdf.countryservice.domain.Timezone;

/** Storage for {@link Timezone timezones}. */
public interface TimezoneRepository {

  /**
   * Stores the given timezones. A timezone is matched to an existing one by its identifier: matches
   * are updated and the others are inserted. Stored timezones that are not in the collection are
   * left untouched. If the collection contains several timezones with the same identifier, the last
   * one wins.
   *
   * @param timezones the timezones to store
   */
  void upsertAll(Collection<Timezone> timezones);
}
