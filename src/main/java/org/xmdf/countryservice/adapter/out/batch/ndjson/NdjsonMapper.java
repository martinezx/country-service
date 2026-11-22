package org.xmdf.countryservice.adapter.out.batch.ndjson;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.CountryLine;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.CurrencyLine;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.LanguageLine;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.RegionLine;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.TimezoneLine;
import org.xmdf.countryservice.domain.Country;
import org.xmdf.countryservice.domain.Currency;
import org.xmdf.countryservice.domain.Language;
import org.xmdf.countryservice.domain.Region;
import org.xmdf.countryservice.domain.Timezone;

/**
 * Maps between the domain model and the lines of the ndjson files. Missing lists are mapped to
 * empty lists, and identifiers are left null because the mapped objects have not been stored.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface NdjsonMapper {

  /**
   * Maps a region name to a region.
   *
   * @param name the name of the region
   * @return the region
   */
  default Region toRegion(String name) {
    return new Region(null, name);
  }

  /**
   * Maps a timezone identifier to a timezone.
   *
   * @param timezone the identifier of the timezone
   * @return the timezone
   */
  default Timezone toTimezone(String timezone) {
    return new Timezone(null, timezone);
  }

  /**
   * Maps a timezone to the identifier written in the line of a country file.
   *
   * @param timezone the timezone
   * @return the identifier of the timezone
   */
  default String toIdentifier(Timezone timezone) {
    return timezone.timezone();
  }

  /**
   * Maps a region to the line of a region file.
   *
   * @param region the region
   * @return the line
   */
  RegionLine toLine(Region region);

  /**
   * Maps a timezone to the line of a timezone file.
   *
   * @param timezone the timezone
   * @return the line
   */
  TimezoneLine toLine(Timezone timezone);

  /**
   * Maps a currency to the line of a currency file.
   *
   * @param currency the currency
   * @return the line
   */
  CurrencyLine toLine(Currency currency);

  /**
   * Maps a language to the line of a language file.
   *
   * @param language the language
   * @return the line
   */
  LanguageLine toLine(Language language);

  /**
   * Maps a country to the line of a country file. The latitude and longitude are written as a list,
   * or as null if the location is unknown, the region is written as its name, and the timezones as
   * their identifiers.
   *
   * @param country the country
   * @return the line
   */
  @Mapping(target = "region", source = "region.name")
  @Mapping(
      target = "latlng",
      expression = "java(toLatlng(country.latitude(), country.longitude()))")
  CountryLine toLine(Country country);

  /**
   * Maps the line of a region file to a region.
   *
   * @param line the line
   * @return the region
   */
  @Mapping(target = "id", ignore = true)
  Region toDomain(RegionLine line);

  /**
   * Maps the line of a timezone file to a timezone.
   *
   * @param line the line
   * @return the timezone
   */
  @Mapping(target = "id", ignore = true)
  Timezone toDomain(TimezoneLine line);

  /**
   * Maps the line of a currency file to a currency.
   *
   * @param line the line
   * @return the currency
   */
  @Mapping(target = "id", ignore = true)
  Currency toDomain(CurrencyLine line);

  /**
   * Maps the line of a language file to a language.
   *
   * @param line the line
   * @return the language
   */
  @Mapping(target = "id", ignore = true)
  Language toDomain(LanguageLine line);

  /**
   * Maps the line of a country file to a country. The location list is split into latitude and
   * longitude, and the region name and the timezone identifiers are mapped to objects.
   *
   * @param line the line
   * @return the country
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "latitude", source = "latlng", qualifiedByName = "latitude")
  @Mapping(target = "longitude", source = "latlng", qualifiedByName = "longitude")
  Country toDomain(CountryLine line);

  /**
   * Combines a latitude and a longitude into the location written to country files.
   *
   * @param latitude the latitude, or null if unknown
   * @param longitude the longitude, or null if unknown
   * @return the latitude and longitude, in this order, or null if either is unknown
   */
  default List<Double> toLatlng(Double latitude, Double longitude) {
    return latitude != null && longitude != null ? List.of(latitude, longitude) : null;
  }

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
