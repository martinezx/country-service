package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.CurrencyEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.CurrencyJpaRepository;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.out.CurrencyRepository;
import org.xmdf.countryservice.domain.Currency;

@Repository
class CurrencyRepositoryAdapter implements CurrencyRepository {

  private final CurrencyJpaRepository jpa;
  private final EntityMapper mapper;
  private final NaturalKeyUpserter<Currency, CurrencyEntity, String> upserter;

  CurrencyRepositoryAdapter(
      EntityManager entityManager, CurrencyJpaRepository jpa, EntityMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
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

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Currency> findAll(PageQuery query) {
    return PagedResults.of(
        jpa.findAll(PageRequest.of(query.page(), query.size(), Sort.by("code"))), mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Currency> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public boolean existsById(Long id) {
    return jpa.existsById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Currency> findByCountryId(Long countryId, PageQuery query) {
    return PagedResults.of(
        jpa.findByCountryId(countryId, PageRequest.of(query.page(), query.size())),
        mapper::toDomain);
  }
}
