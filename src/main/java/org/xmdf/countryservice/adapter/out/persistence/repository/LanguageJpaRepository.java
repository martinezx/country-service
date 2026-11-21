package org.xmdf.countryservice.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.entity.LanguageEntity;

/** Spring Data JPA repository for {@link LanguageEntity}. */
public interface LanguageJpaRepository extends JpaRepository<LanguageEntity, Long> {

  /**
   * Finds the languages with one of the given ISO 639-2 codes.
   *
   * @param iso6392Codes the ISO 639-2 codes to look for
   * @return the matching languages, empty if there are none
   */
  List<LanguageEntity> findByIso6392In(Collection<String> iso6392Codes);
}
