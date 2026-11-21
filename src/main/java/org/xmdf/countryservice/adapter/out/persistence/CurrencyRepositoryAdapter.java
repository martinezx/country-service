package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.CurrencyEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.CurrencyJpaRepository;
import org.xmdf.countryservice.application.port.out.CurrencyRepository;
import org.xmdf.countryservice.domain.Currency;

@Component
class CurrencyRepositoryAdapter implements CurrencyRepository {

  private final NaturalKeyUpserter<Currency, CurrencyEntity, String> upserter;

  CurrencyRepositoryAdapter(
      EntityManager entityManager, CurrencyJpaRepository jpa, EntityMapper mapper) {
    this.upserter =
        new NaturalKeyUpserter<>(
            entityManager,
            Currency::code,
            CurrencyEntity::getCode,
            jpa::findByCodeIn,
            CurrencyEntity::new,
            mapper::update);
  }

  @Override
  @Transactional
  public void upsertAll(Collection<Currency> currencies) {
    upserter.upsertAll(currencies);
  }
}
