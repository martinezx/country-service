package org.xmdf.countryservice.adapter.out.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.xmdf.countryservice.adapter.out.persistence.entity.CountryEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.CurrencyEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.LanguageEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.RegionEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.TimezoneEntity;
import org.xmdf.countryservice.domain.Country;
import org.xmdf.countryservice.domain.Currency;
import org.xmdf.countryservice.domain.Language;
import org.xmdf.countryservice.domain.Region;
import org.xmdf.countryservice.domain.Timezone;

/**
 * Maps between domain objects and JPA entities. Entities are updated in place, so that Hibernate
 * can detect which ones actually changed.
 */
@Mapper
interface EntityMapper {

  /**
   * Copies a region onto an entity.
   *
   * @param region the region to copy from
   * @param entity the entity to update
   */
  void update(Region region, @MappingTarget RegionEntity entity);

  /**
   * Copies a timezone onto an entity, including its offset from UTC.
   *
   * @param timezone the timezone to copy from
   * @param entity the entity to update
   */
  @Mapping(target = "utcOffset", expression = "java(timezone.utcOffset())")
  @Mapping(target = "utcOffsetMinutes", expression = "java(timezone.utcOffsetMinutes())")
  void update(Timezone timezone, @MappingTarget TimezoneEntity entity);

  /**
   * Copies a currency onto an entity.
   *
   * @param currency the currency to copy from
   * @param entity the entity to update
   */
  void update(Currency currency, @MappingTarget CurrencyEntity entity);

  /**
   * Copies a language onto an entity.
   *
   * @param language the language to copy from
   * @param entity the entity to update
   */
  void update(Language language, @MappingTarget LanguageEntity entity);

  /**
   * Copies the values of a country onto an entity. The region, currencies, languages and
   * timezones are references to other entities, so they are not copied and must be set by the
   * caller.
   *
   * @param country the country to copy from
   * @param entity the entity to update
   */
  @Mapping(target = "region", ignore = true)
  @Mapping(target = "currencies", ignore = true)
  @Mapping(target = "languages", ignore = true)
  @Mapping(target = "timezones", ignore = true)
  void update(Country country, @MappingTarget CountryEntity entity);

  /**
   * Maps a region entity to a region.
   *
   * @param entity the entity
   * @return the region
   */
  Region toDomain(RegionEntity entity);

  /**
   * Maps a timezone entity to a timezone.
   *
   * @param entity the entity
   * @return the timezone
   */
  Timezone toDomain(TimezoneEntity entity);

  /**
   * Maps a currency entity to a currency.
   *
   * @param entity the entity
   * @return the currency
   */
  Currency toDomain(CurrencyEntity entity);

  /**
   * Maps a language entity to a language.
   *
   * @param entity the entity
   * @return the language
   */
  Language toDomain(LanguageEntity entity);

  /**
   * Maps a country entity to a country, including its region, currencies and languages. The
   * entity's associations must be loadable, so this must be called within a transaction.
   *
   * @param entity the entity
   * @return the country
   */
  Country toDomain(CountryEntity entity);
}
