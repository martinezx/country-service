package org.xmdf.countryservice.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LanguageRestDto(
    String name,
    @JsonProperty("iso639_1") String iso6391,
    @JsonProperty("iso639_2") String iso6392) {}
