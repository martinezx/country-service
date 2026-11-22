package org.xmdf.countryservice.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  /**
   * Finds the languages spoken in a country, ordered by ISO 639-2 code.
   *
   * @param countryId the identifier of the country
   * @param pageable the page to return, without a sort order
   * @return the requested page of languages
   */
  @Query(
      value =
          "select language from CountryEntity country join country.languages language"
              + " where country.id = :countryId order by language.iso6392",
      countQuery =
          "select count(language) from CountryEntity country join country.languages language"
              + " where country.id = :countryId")
  Page<LanguageEntity> findByCountryId(@Param("countryId") Long countryId, Pageable pageable);
}
