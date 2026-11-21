package org.xmdf.countryservice.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.entity.RegionEntity;

/** Spring Data JPA repository for {@link RegionEntity}. */
public interface RegionJpaRepository extends JpaRepository<RegionEntity, Long> {

  /**
   * Finds the regions with one of the given names.
   *
   * @param names the region names to look for
   * @return the matching regions, empty if there are none
   */
  List<RegionEntity> findByNameIn(Collection<String> names);
}
