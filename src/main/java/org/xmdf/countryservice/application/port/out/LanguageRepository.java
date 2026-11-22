package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
import java.util.Optional;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.domain.Language;

/** Storage for {@link Language languages}. */
public interface LanguageRepository {

  /**
   * Stores the given languages. A language is matched to an existing one by its ISO 639-2 code,
   * because the ISO 639-1 code is missing for some languages: matches are updated and the others
   * are inserted. Stored languages that are not in the collection are left untouched. If the
   * collection contains several languages with the same ISO 639-2 code, the last one wins.
   *
   * @param languages the languages to store
   */
  void upsertAll(Collection<Language> languages);

  /**
   * Finds a page of languages.
   *
   * @param query the page to return
   * @return the page, with the languages ordered by ISO 639-2 code
   */
  PagedResult<Language> findAll(PageQuery query);

  /**
   * Finds a language by its identifier.
   *
   * @param id the identifier of the language
   * @return the language, or empty if there is none with that identifier
   */
  Optional<Language> findById(Long id);

  /**
   * Tells whether a language with the given identifier exists.
   *
   * @param id the identifier of the language
   * @return true if the language exists
   */
  boolean existsById(Long id);

  /**
   * Finds a page of the languages spoken in a country.
   *
   * @param countryId the identifier of the country
   * @param query the page to return
   * @return the page, with the languages ordered by ISO 639-2 code. It is empty if the country does
   *     not exist or has no languages.
   */
  PagedResult<Language> findByCountryId(Long countryId, PageQuery query);
}
