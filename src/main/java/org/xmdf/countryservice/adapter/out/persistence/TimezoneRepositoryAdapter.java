package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.TimezoneEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.TimezoneJpaRepository;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.out.TimezoneRepository;
import org.xmdf.countryservice.domain.Timezone;

@Repository
class TimezoneRepositoryAdapter implements TimezoneRepository {

  private final TimezoneJpaRepository jpa;
  private final EntityMapper mapper;
  private final NaturalKeyUpserter<Timezone, TimezoneEntity, String> upserter;

  TimezoneRepositoryAdapter(
      EntityManager entityManager, TimezoneJpaRepository jpa, EntityMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
    this.upserter =
        new NaturalKeyUpserter<>(
            entityManager,
            Timezone::timezone,
            TimezoneEntity::getTimezone,
            jpa::findByTimezoneIn,
            TimezoneEntity::new,
            mapper::update);
  }

  @Override
  @Transactional
  public void upsertAll(Collection<Timezone> timezones) {
    upserter.upsertAll(timezones);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Timezone> findAll(PageQuery query) {
    return PagedResults.of(
        jpa.findAll(PageRequest.of(query.page(), query.size(), Sort.by("timezone"))),
        mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Timezone> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public boolean existsById(Long id) {
    return jpa.existsById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Timezone> findByCountryId(Long countryId, PageQuery query) {
    return PagedResults.of(
        jpa.findByCountryId(countryId, PageRequest.of(query.page(), query.size())),
        mapper::toDomain);
  }
}
