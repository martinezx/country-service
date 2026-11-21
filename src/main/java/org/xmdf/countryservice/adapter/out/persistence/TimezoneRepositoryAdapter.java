package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.TimezoneEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.TimezoneJpaRepository;
import org.xmdf.countryservice.application.port.out.TimezoneRepository;
import org.xmdf.countryservice.domain.Timezone;

@Component
class TimezoneRepositoryAdapter implements TimezoneRepository {

  private final NaturalKeyUpserter<Timezone, TimezoneEntity, String> upserter;

  TimezoneRepositoryAdapter(
      EntityManager entityManager, TimezoneJpaRepository jpa, EntityMapper mapper) {
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
}
