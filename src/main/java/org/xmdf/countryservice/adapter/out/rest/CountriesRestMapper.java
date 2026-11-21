package org.xmdf.countryservice.adapter.out.rest;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.xmdf.countryservice.adapter.out.rest.dto.CountryRestDto;
import org.xmdf.countryservice.adapter.out.rest.dto.CurrencyRestDto;
import org.xmdf.countryservice.adapter.out.rest.dto.LanguageRestDto;
import org.xmdf.countryservice.adapter.out.rest.dto.TimezoneRestDto;
import org.xmdf.countryservice.domain.Country;
import org.xmdf.countryservice.domain.Currency;
import org.xmdf.countryservice.domain.Language;
import org.xmdf.countryservice.domain.Region;
import org.xmdf.countryservice.domain.Timezone;

/**
 * Maps the countries.dev response types to the domain model. Missing lists are mapped to empty
 * lists.
 */
@Mapper(nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
interface CountriesRestMapper {

  /**
   * Maps a region name to a region.
   *
   * @param name the name of the region
   * @return the region
   */
  default Region toRegion(String name) {
    return new Region(name);
  }

  /**
   * Maps timezone responses to timezones.
   *
   * @param timezones the timezone responses
   * @return the timezones, in the same order
   */
  List<Timezone> toTimezones(List<TimezoneRestDto> timezones);

  /**
   * Maps region names to regions.
   *
   * @param names the names of the regions
   * @return the regions, in the same order
   */
  List<Region> toRegions(List<String> names);

  /**
   * Maps currency responses to currencies.
   *
   * @param currencies the currency responses
   * @return the currencies, in the same order, or an empty list if the argument is null
   */
  List<Currency> toCurrencies(List<CurrencyRestDto> currencies);

  /**
   * Maps language responses to languages.
   *
   * @param languages the language responses
   * @return the languages, in the same order, or an empty list if the argument is null
   */
  List<Language> toLanguages(List<LanguageRestDto> languages);

  /**
   * Maps country responses to countries.
   *
   * @param countries the country responses
   * @return the countries, in the same order
   */
  List<Country> toCountries(List<CountryRestDto> countries);

  /**
   * Maps a timezone response to a timezone.
   *
   * @param timezone the timezone response
   * @return the timezone
   */
  Timezone toTimezone(TimezoneRestDto timezone);

  /**
   * Maps a currency response to a currency.
   *
   * @param currency the currency response
   * @return the currency
   */
  Currency toCurrency(CurrencyRestDto currency);

  /**
   * Maps a language response to a language.
   *
   * @param language the language response
   * @return the language
   */
  Language toLanguage(LanguageRestDto language);

  /**
   * Maps a country response to a country. The location of the response, a list of latitude and
   * longitude, is split into the two coordinates of the country.
   *
   * @param country the country response
   * @return the country
   */
  @Mapping(target = "latitude", source = "latlng", qualifiedByName = "latitude")
  @Mapping(target = "longitude", source = "latlng", qualifiedByName = "longitude")
  Country toCountry(CountryRestDto country);

  /**
   * Extracts the latitude from a location.
   *
   * @param latlng the latitude and longitude, in this order, or null if unknown
   * @return the latitude, or null if the location is unknown
   */
  @Named("latitude")
  default Double latitude(List<Double> latlng) {
    return latlng != null && latlng.size() >= 2 ? latlng.getFirst() : null;
  }

  /**
   * Extracts the longitude from a location.
   *
   * @param latlng the latitude and longitude, in this order, or null if unknown
   * @return the longitude, or null if the location is unknown
   */
  @Named("longitude")
  default Double longitude(List<Double> latlng) {
    return latlng != null && latlng.size() >= 2 ? latlng.get(1) : null;
  }
}
