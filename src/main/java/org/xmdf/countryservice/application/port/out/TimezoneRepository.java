package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
import java.util.Optional;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.domain.Timezone;
import org.xmdf.countryservice.domain.UnsupportedTimezoneException;

/** Storage for {@link Timezone timezones}. */
public interface TimezoneRepository {

  /**
   * Stores the given timezones. A timezone is matched to an existing one by its identifier: matches
   * are updated and the others are inserted. Stored timezones that are not in the collection are
   * left untouched. If the collection contains several timezones with the same identifier, the last
   * one wins.
   *
   * @param timezones the timezones to store
   * @throws UnsupportedTimezoneException if the identifier of a timezone is not supported
   */
  void upsertAll(Collection<Timezone> timezones);

  /**
   * Finds a page of timezones.
   *
   * @param query the page to return
   * @return the page, with the timezones ordered by their identifier
   */
  PagedResult<Timezone> findAll(PageQuery query);

  /**
   * Finds a timezone by its identifier.
   *
   * @param id the identifier of the timezone
   * @return the timezone, or empty if there is none with that identifier
   */
  Optional<Timezone> findById(Long id);

  /**
   * Tells whether a timezone with the given identifier exists.
   *
   * @param id the identifier of the timezone
   * @return true if the timezone exists
   */
  boolean existsById(Long id);

  /**
   * Finds a page of the timezones of a country.
   *
   * @param countryId the identifier of the country
   * @param query the page to return
   * @return the page, with the timezones ordered by their identifier. It is empty if the country
   *     does not exist or has no timezones.
   */
  PagedResult<Timezone> findByCountryId(Long countryId, PageQuery query);
}
