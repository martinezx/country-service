package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.CountryEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.CurrencyEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.LanguageEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.RegionEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.TimezoneEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.CountryJpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.repository.CurrencyJpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.repository.LanguageJpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.repository.RegionJpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.repository.TimezoneJpaRepository;
import org.xmdf.countryservice.application.exception.InvalidRecordException;
import org.xmdf.countryservice.application.model.PageQuery;
import org.xmdf.countryservice.application.model.PagedResult;
import org.xmdf.countryservice.application.port.out.CountryRepository;
import org.xmdf.countryservice.domain.Country;
import org.xmdf.countryservice.domain.Currency;
import org.xmdf.countryservice.domain.Language;
import org.xmdf.countryservice.domain.Timezone;

@Repository
class CountryRepositoryAdapter implements CountryRepository {

  private final EntityManager entityManager;
  private final EntityMapper mapper;
  private final CountryJpaRepository countries;
  private final RegionJpaRepository regions;
  private final CurrencyJpaRepository currencies;
  private final LanguageJpaRepository languages;
  private final TimezoneJpaRepository timezones;

  CountryRepositoryAdapter(
      EntityManager entityManager,
      EntityMapper mapper,
      CountryJpaRepository countries,
      RegionJpaRepository regions,
      CurrencyJpaRepository currencies,
      LanguageJpaRepository languages,
      TimezoneJpaRepository timezones) {
    this.entityManager = entityManager;
    this.mapper = mapper;
    this.countries = countries;
    this.regions = regions;
    this.currencies = currencies;
    this.languages = languages;
    this.timezones = timezones;
  }

  @Override
  @Transactional
  public void upsertAll(Collection<Country> items) {
    Map<String, Country> byAlpha2Code = new LinkedHashMap<>();
    Set<String> regionNames = new HashSet<>();
    Set<String> currencyCodes = new HashSet<>();
    Set<String> languageCodes = new HashSet<>();
    Set<String> timezoneIds = new HashSet<>();
    for (Country country : items) {
      byAlpha2Code.put(country.alpha2Code(), country);
      regionNames.add(country.region().name());
      country.currencies().forEach(c -> currencyCodes.add(c.code()));
      country.languages().forEach(l -> languageCodes.add(l.iso6392()));
      country.timezones().forEach(t -> timezoneIds.add(t.timezone()));
    }

    Map<String, CountryEntity> existing =
        index(countries.findByAlpha2CodeIn(byAlpha2Code.keySet()), CountryEntity::getAlpha2Code);
    Map<String, RegionEntity> regionsByName =
        index(regions.findByNameIn(regionNames), RegionEntity::getName);
    Map<String, CurrencyEntity> currenciesByCode =
        index(currencies.findByCodeIn(currencyCodes), CurrencyEntity::getCode);
    Map<String, LanguageEntity> languagesByCode =
        index(languages.findByIso6392In(languageCodes), LanguageEntity::getIso6392);
    Map<String, TimezoneEntity> timezonesById =
        index(timezones.findByTimezoneIn(timezoneIds), TimezoneEntity::getTimezone);

    byAlpha2Code.forEach((alpha2Code, country) -> {
      CountryEntity entity = existing.get(alpha2Code);
      boolean isNew = entity == null;
      if (isNew) {
        entity = new CountryEntity();
      }
      map(country, entity, regionsByName);
      for (Currency currency : country.currencies()) {
        entity.getCurrencies().add(currencyEntity(currency, currenciesByCode));
      }
      for (Language language : country.languages()) {
        entity.getLanguages().add(languageEntity(language, languagesByCode));
      }
      for (Timezone timezone : country.timezones()) {
        entity.getTimezones().add(timezoneEntity(timezone, timezonesById));
      }
      if (isNew) {
        entityManager.persist(entity);
      }
    });
    entityManager.flush();
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Country> findAll(PageQuery query) {
    return PagedResults.of(countries.findAll(pageRequest(query)), mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Country> findById(Long id) {
    return countries.findById(id).map(mapper::toDomain);
  }

  @Override
  public boolean existsById(Long id) {
    return countries.existsById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Country> findByRegionId(Long regionId, PageQuery query) {
    return PagedResults.of(
        countries.findByRegionId(regionId, pageRequest(query)), mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Country> findByTimezoneId(Long timezoneId, PageQuery query) {
    return PagedResults.of(
        countries.findByTimezonesId(timezoneId, pageRequest(query)), mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Country> findByCurrencyId(Long currencyId, PageQuery query) {
    return PagedResults.of(
        countries.findByCurrenciesId(currencyId, pageRequest(query)), mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResult<Country> findByLanguageId(Long languageId, PageQuery query) {
    return PagedResults.of(
        countries.findByLanguagesId(languageId, pageRequest(query)), mapper::toDomain);
  }

  private static PageRequest pageRequest(PageQuery query) {
    return PageRequest.of(query.page(), query.size(), Sort.by("alpha2Code"));
  }

  private void map(Country country, CountryEntity entity, Map<String, RegionEntity> regionsByName) {
    RegionEntity region = regionsByName.get(country.region().name());
    if (region == null) {
      throw new InvalidRecordException(
          "Region '%s' of country %s does not exist"
              .formatted(country.region().name(), country.alpha2Code()));
    }
    mapper.update(country, entity);
    entity.setRegion(region);
  }

  private static CurrencyEntity currencyEntity(
      Currency currency, Map<String, CurrencyEntity> currenciesByCode) {
    CurrencyEntity entity = currenciesByCode.get(currency.code());
    if (entity == null) {
      throw new InvalidRecordException("Currency '%s' does not exist".formatted(currency.code()));
    }
    return entity;
  }

  private static TimezoneEntity timezoneEntity(
      Timezone timezone, Map<String, TimezoneEntity> timezonesById) {
    TimezoneEntity entity = timezonesById.get(timezone.timezone());
    if (entity == null) {
      throw new InvalidRecordException(
          "Timezone '%s' does not exist".formatted(timezone.timezone()));
    }
    return entity;
  }

  private LanguageEntity languageEntity(
      Language language, Map<String, LanguageEntity> languagesByCode) {
    LanguageEntity entity = languagesByCode.get(language.iso6392());
    if (entity == null) {
      entity = new LanguageEntity();
      mapper.update(language, entity);
      entityManager.persist(entity);
      languagesByCode.put(language.iso6392(), entity);
    }
    return entity;
  }

  private static <E> Map<String, E> index(List<E> entities, Function<E, String> key) {
    return entities.stream().collect(Collectors.toMap(key, Function.identity()));
  }
}
