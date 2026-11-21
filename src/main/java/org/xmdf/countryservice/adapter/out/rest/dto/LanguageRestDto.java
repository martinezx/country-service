package org.xmdf.countryservice.adapter.out.rest.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LanguageRestDto(
    String name,
    @JsonProperty("iso6391") @JsonAlias("iso639_1") String iso6391,
    @JsonProperty("iso6392") @JsonAlias("iso639_2") String iso6392) {}
