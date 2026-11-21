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
 * Copies the values of domain objects onto JPA entities. Entities are updated in place, so that
 * Hibernate can detect which ones actually changed.
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
   * Copies a timezone onto an entity.
   *
   * @param timezone the timezone to copy from
   * @param entity the entity to update
   */
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
   * Copies the values of a country onto an entity. The region, currencies and languages are
   * references to other entities, so they are not copied and must be set by the caller.
   *
   * @param country the country to copy from
   * @param entity the entity to update
   */
  @Mapping(target = "region", ignore = true)
  @Mapping(target = "currencies", ignore = true)
  @Mapping(target = "languages", ignore = true)
  void update(Country country, @MappingTarget CountryEntity entity);
}
