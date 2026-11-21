package org.xmdf.countryservice.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
