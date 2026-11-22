package org.xmdf.countryservice.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.xmdf.countryservice.adapter.out.persistence.entity.TimezoneEntity;

/** Spring Data JPA repository for {@link TimezoneEntity}. */
public interface TimezoneJpaRepository extends JpaRepository<TimezoneEntity, Long> {

  /**
   * Finds the timezones with one of the given identifiers.
   *
   * @param timezones the timezone identifiers to look for
   * @return the matching timezones, empty if there are none
   */
  List<TimezoneEntity> findByTimezoneIn(Collection<String> timezones);

  /**
   * Finds the timezones of a country, ordered by identifier.
   *
   * @param countryId the identifier of the country
   * @param pageable the page to return, without a sort order
   * @return the requested page of timezones
   */
  @Query(
      value =
          "select timezone from CountryEntity country join country.timezones timezone"
              + " where country.id = :countryId order by timezone.timezone",
      countQuery =
          "select count(timezone) from CountryEntity country join country.timezones timezone"
              + " where country.id = :countryId")
  Page<TimezoneEntity> findByCountryId(@Param("countryId") Long countryId, Pageable pageable);
}
