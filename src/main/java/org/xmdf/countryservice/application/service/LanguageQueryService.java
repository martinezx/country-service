package org.xmdf.countryservice.application.service;

import org.springframework.stereotype.Service;
import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.in.LanguageQueryUseCase;
import org.xmdf.countryservice.application.port.out.CountryRepository;
import org.xmdf.countryservice.application.port.out.LanguageRepository;
import org.xmdf.countryservice.domain.Language;

@Service
public class LanguageQueryService implements LanguageQueryUseCase {

  private final LanguageRepository languages;
  private final CountryRepository countries;

  public LanguageQueryService(LanguageRepository languages, CountryRepository countries) {
    this.languages = languages;
    this.countries = countries;
  }

  @Override
  public PagedResult<Language> findAll(PageQuery query) {
    return languages.findAll(query);
  }

  @Override
  public Language getById(Long id) {
    return languages.findById(id).orElseThrow(() -> new ResourceNotFoundException("Language", id));
  }

  @Override
  public PagedResult<Language> findByCountry(Long countryId, PageQuery query) {
    if (!countries.existsById(countryId)) {
      throw new ResourceNotFoundException("Country", countryId);
    }
    return languages.findByCountryId(countryId, query);
  }
}
