package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.RegionEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.RegionJpaRepository;
import org.xmdf.countryservice.application.port.out.RegionRepository;
import org.xmdf.countryservice.domain.Region;

@Repository
class RegionRepositoryAdapter implements RegionRepository {

  private final RegionJpaRepository jpa;
  private final EntityMapper mapper;
  private final NaturalKeyUpserter<Region, RegionEntity, String> upserter;

  RegionRepositoryAdapter(
      EntityManager entityManager, RegionJpaRepository jpa, EntityMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
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

  @Override
  @Transactional(readOnly = true)
  public List<Region> findAll() {
    return jpa.findAll(Sort.by("name")).stream().map(mapper::toDomain).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Region> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public boolean existsById(Long id) {
    return jpa.existsById(id);
  }
}
