package org.xmdf.countryservice.domain;

import java.util.List;

/**
 * A country or territory.
 *
 * @param alpha2Code the ISO 3166-1 alpha-2 code, which identifies the country
 * @param alpha3Code the ISO 3166-1 alpha-3 code
 * @param numericCode the ISO 3166-1 numeric code, as a string with leading zeros preserved
 * @param name the name of the country
 * @param region the region the country belongs to
 * @param latitude the latitude of the country, or null if its location is unknown
 * @param longitude the longitude of the country, or null if its location is unknown
 * @param currencies the currencies used in the country, empty if there are none
 * @param languages the languages spoken in the country, empty if there are none
 */
public record Country(
    String alpha2Code,
    String alpha3Code,
    String numericCode,
    String name,
    Region region,
    Double latitude,
    Double longitude,
    List<Currency> currencies,
    List<Language> languages) {}
