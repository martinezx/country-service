package org.xmdf.countryservice.adapter.out.batch.load;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonMapper;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.CountryLine;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.CurrencyLine;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.LanguageLine;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.RegionLine;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonRecords.TimezoneLine;
import org.xmdf.countryservice.adapter.out.batch.shared.ExportEntity;
import org.xmdf.countryservice.application.port.out.CountryRepository;
import org.xmdf.countryservice.application.port.out.CurrencyRepository;
import org.xmdf.countryservice.application.port.out.LanguageRepository;
import org.xmdf.countryservice.application.port.out.RegionRepository;
import org.xmdf.countryservice.application.port.out.TimezoneRepository;

@Component
class StoreItemWriters {

  private final ObjectMapper objectMapper;
  private final NdjsonMapper mapper;
  private final RegionRepository regions;
  private final TimezoneRepository timezones;
  private final CurrencyRepository currencies;
  private final LanguageRepository languages;
  private final CountryRepository countries;

  StoreItemWriters(
      ObjectMapper objectMapper,
      NdjsonMapper mapper,
      RegionRepository regions,
      TimezoneRepository timezones,
      CurrencyRepository currencies,
      LanguageRepository languages,
      CountryRepository countries) {
    this.objectMapper = objectMapper;
    this.mapper = mapper;
    this.regions = regions;
    this.timezones = timezones;
    this.currencies = currencies;
    this.languages = languages;
    this.countries = countries;
  }

  StoreItemWriter<?, ?> forEntity(ExportEntity entity) {
    return switch (entity) {
      case REGION -> this.writer(RegionLine.class, mapper::toDomain, regions::upsertAll);
      case TIMEZONE -> this.writer(TimezoneLine.class, mapper::toDomain, timezones::upsertAll);
      case CURRENCY -> this.writer(CurrencyLine.class, mapper::toDomain, currencies::upsertAll);
      case LANGUAGE -> this.writer(LanguageLine.class, mapper::toDomain, languages::upsertAll);
      case COUNTRY -> this.writer(CountryLine.class, mapper::toDomain, countries::upsertAll);
    };
  }

  private <L, D> StoreItemWriter<L, D> writer(
      Class<L> lineType, Function<L, D> toDomain, Consumer<List<D>> store) {
    return new StoreItemWriter<>(objectMapper, lineType, toDomain, store);
  }
}
