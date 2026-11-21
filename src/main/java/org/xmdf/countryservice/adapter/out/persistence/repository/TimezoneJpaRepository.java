package org.xmdf.countryservice.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
