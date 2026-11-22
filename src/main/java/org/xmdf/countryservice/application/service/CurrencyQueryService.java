package org.xmdf.countryservice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.in.CurrencyQueryUseCase;
import org.xmdf.countryservice.application.port.out.CountryRepository;
import org.xmdf.countryservice.application.port.out.CurrencyRepository;
import org.xmdf.countryservice.domain.Currency;

@Service
@RequiredArgsConstructor
public class CurrencyQueryService implements CurrencyQueryUseCase {

  private final CurrencyRepository currencies;
  private final CountryRepository countries;

  @Override
  public PagedResult<Currency> findAll(PageQuery query) {
    return currencies.findAll(query);
  }

  @Override
  public Currency getById(Long id) {
    return currencies.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency", id));
  }

  @Override
  public PagedResult<Currency> findByCountry(Long countryId, PageQuery query) {
    if (!countries.existsById(countryId)) {
      throw new ResourceNotFoundException("Country", countryId);
    }
    return currencies.findByCountryId(countryId, query);
  }
}
