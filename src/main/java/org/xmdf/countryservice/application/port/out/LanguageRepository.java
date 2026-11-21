package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
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
}
