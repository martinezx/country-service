package org.xmdf.countryservice.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.entity.CountryEntity;

/** Spring Data JPA repository for {@link CountryEntity}. */
public interface CountryJpaRepository extends JpaRepository<CountryEntity, Long> {

  /**
   * Finds the countries with one of the given alpha-2 codes. The currencies and languages of each
   * country are loaded together with it.
   *
   * @param alpha2Codes the alpha-2 codes to look for
   * @return the matching countries, empty if there are none
   */
  @EntityGraph(attributePaths = {"currencies", "languages"})
  List<CountryEntity> findByAlpha2CodeIn(Collection<String> alpha2Codes);

  /**
   * Finds the countries in a region.
   *
   * @param regionId the identifier of the region
   * @param pageable the page to return, including its sort order
   * @return the requested page of countries
   */
  Page<CountryEntity> findByRegionId(Long regionId, Pageable pageable);

  /**
   * Finds the countries that use a currency.
   *
   * @param currencyId the identifier of the currency
   * @param pageable the page to return, including its sort order
   * @return the requested page of countries
   */
  Page<CountryEntity> findByCurrenciesId(Long currencyId, Pageable pageable);

  /**
   * Finds the countries where a language is spoken.
   *
   * @param languageId the identifier of the language
   * @param pageable the page to return, including its sort order
   * @return the requested page of countries
   */
  Page<CountryEntity> findByLanguagesId(Long languageId, Pageable pageable);
}
