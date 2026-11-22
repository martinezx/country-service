package org.xmdf.countryservice.adapter.in.web.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

/**
 * Representation of a country. Its region, currencies, languages and timezones are not part of the
 * representation, they are reachable through its links.
 *
 * @param id the identifier of the country, used in the URLs of the API
 * @param alpha2Code the ISO 3166-1 alpha-2 code
 * @param alpha3Code the ISO 3166-1 alpha-3 code
 * @param numericCode the ISO 3166-1 numeric code, as a string with leading zeros preserved
 * @param name the name of the country
 * @param latitude the latitude of the country, or null if its location is unknown
 * @param longitude the longitude of the country, or null if its location is unknown
 */
@Relation(itemRelation = "country", collectionRelation = "countries")
public record CountryResponse(
    @Schema(
            description = "Identifier of the country, used in the URLs of the API",
            requiredMode = REQUIRED)
        Long id,
    @Schema(description = "ISO 3166-1 alpha-2 code", example = "ES", requiredMode = REQUIRED)
        String alpha2Code,
    @Schema(description = "ISO 3166-1 alpha-3 code", example = "ESP", requiredMode = REQUIRED)
        String alpha3Code,
    @Schema(
            description = "ISO 3166-1 numeric code, with leading zeros preserved",
            example = "724",
            requiredMode = REQUIRED)
        String numericCode,
    @Schema(description = "Name of the country", example = "Spain", requiredMode = REQUIRED)
        String name,
    @Schema(
            description = "Latitude of the country, null if its location is unknown",
            nullable = true,
            requiredMode = REQUIRED)
        Double latitude,
    @Schema(
            description = "Longitude of the country, null if its location is unknown",
            nullable = true,
            requiredMode = REQUIRED)
        Double longitude) {}
