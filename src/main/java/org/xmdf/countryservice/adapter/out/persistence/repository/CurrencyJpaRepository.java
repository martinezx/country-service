package org.xmdf.countryservice.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.xmdf.countryservice.adapter.out.persistence.entity.CurrencyEntity;

/** Spring Data JPA repository for {@link CurrencyEntity}. */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

  /**
   * Finds the currencies with one of the given codes.
   *
   * @param codes the currency codes to look for
   * @return the matching currencies, empty if there are none
   */
  List<CurrencyEntity> findByCodeIn(Collection<String> codes);

  /**
   * Finds the currencies used in a country, ordered by code.
   *
   * @param countryId the identifier of the country
   * @param pageable the page to return, without a sort order
   * @return the requested page of currencies
   */
  @Query(
      value =
          "select currency from CountryEntity country join country.currencies currency"
              + " where country.id = :countryId order by currency.code",
      countQuery =
          "select count(currency) from CountryEntity country join country.currencies currency"
              + " where country.id = :countryId")
  Page<CurrencyEntity> findByCountryId(@Param("countryId") Long countryId, Pageable pageable);
}
