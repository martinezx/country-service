package org.xmdf.countryservice.adapter.in.web.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

/**
 * Representation of a timezone.
 *
 * @param id the identifier of the timezone, used in the URLs of the API
 * @param timezone the identifier of the timezone, for example {@code UTC+01:00}
 * @param utcOffset the offset from UTC as text, for example {@code +01:00}
 * @param utcOffsetMinutes the offset from UTC in minutes, negative west of UTC
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
        String timezone,
    @Schema(
            description = "Offset from UTC as text, +00:00 for no offset",
            example = "+05:30",
            requiredMode = REQUIRED)
        String utcOffset,
    @Schema(
            description = "Offset from UTC in minutes, negative west of UTC",
            example = "330",
            requiredMode = REQUIRED)
        Integer utcOffsetMinutes) {}
