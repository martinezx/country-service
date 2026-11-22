package org.xmdf.countryservice.application.port.in;

import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.domain.Language;

/** Use case that reads the stored languages. */
public interface LanguageQueryUseCase {

  /**
   * Returns a page of languages.
   *
   * @param query the page to return
   * @return the page, with the languages ordered by ISO 639-2 code
   */
  PagedResult<Language> findAll(PageQuery query);

  /**
   * Returns a language.
   *
   * @param id the identifier of the language
   * @return the language
   * @throws ResourceNotFoundException if there is no language with that identifier
   */
  Language getById(Long id);

  /**
   * Returns a page of the languages spoken in a country.
   *
   * @param countryId the identifier of the country
   * @param query the page to return
   * @return the page, with the languages ordered by ISO 639-2 code
   * @throws ResourceNotFoundException if there is no country with that identifier
   */
  PagedResult<Language> findByCountry(Long countryId, PageQuery query);
}
