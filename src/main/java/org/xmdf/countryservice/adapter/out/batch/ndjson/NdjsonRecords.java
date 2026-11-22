package org.xmdf.countryservice.adapter.out.batch.ndjson;

import java.util.List;

public final class NdjsonRecords {

  private NdjsonRecords() {}

  public record RegionLine(String name) {}

  public record TimezoneLine(String timezone) {}

  public record CurrencyLine(String code, String name, String symbol) {}

  public record LanguageLine(String name, String iso6391, String iso6392) {}

  public record CountryLine(
      String region,
      String name,
      List<Double> latlng,
      String numericCode,
      String alpha2Code,
      String alpha3Code,
      List<LanguageLine> languages,
      List<CurrencyLine> currencies,
      List<String> timezones) {}
}
