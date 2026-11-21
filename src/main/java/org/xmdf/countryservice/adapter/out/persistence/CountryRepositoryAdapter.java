package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xmdf.countryservice.adapter.out.persistence.entity.CountryEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.CurrencyEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.LanguageEntity;
import org.xmdf.countryservice.adapter.out.persistence.entity.RegionEntity;
import org.xmdf.countryservice.adapter.out.persistence.repository.CountryJpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.repository.CurrencyJpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.repository.LanguageJpaRepository;
import org.xmdf.countryservice.adapter.out.persistence.repository.RegionJpaRepository;
import org.xmdf.countryservice.application.port.out.CountryRepository;
import org.xmdf.countryservice.domain.Country;
import org.xmdf.countryservice.domain.Currency;
import org.xmdf.countryservice.domain.Language;

@Component
class CountryRepositoryAdapter implements CountryRepository {

  private final EntityManager entityManager;
  private final EntityMapper mapper;
  private final CountryJpaRepository countries;
  private final RegionJpaRepository regions;
  private final CurrencyJpaRepository currencies;
  private final LanguageJpaRepository languages;

  CountryRepositoryAdapter(
      EntityManager entityManager,
      EntityMapper mapper,
      CountryJpaRepository countries,
      RegionJpaRepository regions,
      CurrencyJpaRepository currencies,
      LanguageJpaRepository languages) {
    this.entityManager = entityManager;
    this.mapper = mapper;
    this.countries = countries;
    this.regions = regions;
    this.currencies = currencies;
    this.languages = languages;
  }

  @Override
  @Transactional
  public void upsertAll(Collection<Country> items) {
    Map<String, Country> byAlpha2Code = new LinkedHashMap<>();
    Set<String> regionNames = new HashSet<>();
    Set<String> currencyCodes = new HashSet<>();
    Set<String> languageCodes = new HashSet<>();
    for (Country country : items) {
      byAlpha2Code.put(country.alpha2Code(), country);
      regionNames.add(country.region().name());
      country.currencies().forEach(c -> currencyCodes.add(c.code()));
      country.languages().forEach(l -> languageCodes.add(l.iso6392()));
    }

    Map<String, CountryEntity> existing =
        index(countries.findByAlpha2CodeIn(byAlpha2Code.keySet()), CountryEntity::getAlpha2Code);
    Map<String, RegionEntity> regionsByName =
        index(regions.findByNameIn(regionNames), RegionEntity::getName);
    Map<String, CurrencyEntity> currenciesByCode =
        index(currencies.findByCodeIn(currencyCodes), CurrencyEntity::getCode);
    Map<String, LanguageEntity> languagesByCode =
        index(languages.findByIso6392In(languageCodes), LanguageEntity::getIso6392);

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
      if (isNew) {
        entityManager.persist(entity);
      }
    });
    entityManager.flush();
  }

  private void map(Country country, CountryEntity entity, Map<String, RegionEntity> regionsByName) {
    RegionEntity region = regionsByName.get(country.region().name());
    if (region == null) {
      throw new IllegalStateException(
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
      throw new IllegalStateException("Currency '%s' does not exist".formatted(currency.code()));
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
