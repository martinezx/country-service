package org.xmdf.countryservice.adapter.in.web.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

/**
 * Representation of a timezone.
 *
 * @param id the identifier of the timezone, used in the URLs of the API
 * @param timezone the identifier of the timezone, for example {@code UTC+01:00}
 */
@Relation(itemRelation = "timezone", collectionRelation = "timezones")
public record TimezoneResponse(
    @Schema(
            description = "Identifier of the timezone, used in the URLs of the API",
            requiredMode = REQUIRED)
        Long id,
    @Schema(
            description = "Timezone as published by countries.dev",
            example = "UTC+01:00",
            requiredMode = REQUIRED)
        String timezone) {}
