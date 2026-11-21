package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.RegionEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.RegionJpaRepository;
import org.xmdf.countryservice.application.port.out.RegionRepository;
import org.xmdf.countryservice.domain.Region;

@Component
class RegionRepositoryAdapter implements RegionRepository {

  private final NaturalKeyUpserter<Region, RegionEntity, String> upserter;

  RegionRepositoryAdapter(
      EntityManager entityManager, RegionJpaRepository jpa, EntityMapper mapper) {
    this.upserter =
        new NaturalKeyUpserter<>(
            entityManager,
            Region::name,
            RegionEntity::getName,
            jpa::findByNameIn,
            RegionEntity::new,
            mapper::update);
  }

  @Override
  @Transactional
  public void upsertAll(Collection<Region> regions) {
    upserter.upsertAll(regions);
  }
}
