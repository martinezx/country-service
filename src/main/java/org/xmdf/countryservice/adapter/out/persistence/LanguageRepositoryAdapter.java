package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.LanguageEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.LanguageJpaRepository;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.out.LanguageRepository;
import org.xmdf.countryservice.domain.Language;

@Component
class LanguageRepositoryAdapter implements LanguageRepository {

  private final LanguageJpaRepository jpa;
  private final EntityMapper mapper;
  private final NaturalKeyUpserter<Language, LanguageEntity, String> upserter;

  LanguageRepositoryAdapter(
      EntityManager entityManager, LanguageJpaRepository jpa, EntityMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
    this.upserter =
        new NaturalKeyUpserter<>(
            entityManager,
            Language::iso6392,
            LanguageEntity::getIso6392,
            jpa::findByIso6392In,
            LanguageEntity::new,
            mapper::update);
  }

  @Override
  @Transactional
  public void upsertAll(Collection<Language> languages) {
    upserter.upsertAll(languages);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Language> findAll(PageQuery query) {
    return PagedResults.of(
        jpa.findAll(PageRequest.of(query.page(), query.size(), Sort.by("iso6392"))),
        mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Language> findById(Long id) {
    return jpa.findById(id).map(mapper::toDomain);
  }

  @Override
  public boolean existsById(Long id) {
    return jpa.existsById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Language> findByCountryId(Long countryId, PageQuery query) {
    return PagedResults.of(
        jpa.findByCountryId(countryId, PageRequest.of(query.page(), query.size())),
        mapper::toDomain);
  }
}
