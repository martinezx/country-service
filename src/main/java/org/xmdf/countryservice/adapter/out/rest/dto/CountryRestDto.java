package org.xmdf.countryservice.adapter.out.rest.dto;

import java.util.List;

public record CountryRestDto(
    String region,
    String name,
    List<Double> latlng,
    String numericCode,
    String alpha2Code,
    String alpha3Code,
    List<LanguageRestDto> languages,
    List<CurrencyRestDto> currencies,
    List<String> timezones) {}
