package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.LanguageEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.LanguageJpaRepository;
import org.xmdf.countryservice.application.port.out.LanguageRepository;
import org.xmdf.countryservice.domain.Language;

@Component
class LanguageRepositoryAdapter implements LanguageRepository {

  private final NaturalKeyUpserter<Language, LanguageEntity, String> upserter;

  LanguageRepositoryAdapter(
      EntityManager entityManager, LanguageJpaRepository jpa, EntityMapper mapper) {
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
}
