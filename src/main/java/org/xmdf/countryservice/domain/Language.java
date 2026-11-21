package org.xmdf.countryservice.domain;

/**
 * A language.
 *
 * @param iso6392 the ISO 639-2 code of the language, which identifies it. It is always present.
 * @param iso6391 the ISO 639-1 code of the language, or null if the language has none. Several
 *     languages can share the same ISO 639-1 code.
 * @param name the name of the language
 */
public record Language(String iso6392, String iso6391, String name) {}
