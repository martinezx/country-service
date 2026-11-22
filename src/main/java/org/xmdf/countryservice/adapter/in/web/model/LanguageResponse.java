package org.xmdf.countryservice.adapter.in.web.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

/**
 * Representation of a language.
 *
 * @param id the identifier of the language, used in the URLs of the API
 * @param iso6392 the ISO 639-2 code of the language
 * @param iso6391 the ISO 639-1 code of the language, or null if it has none
 * @param name the name of the language
 */
@Relation(itemRelation = "language", collectionRelation = "languages")
public record LanguageResponse(
    @Schema(
            description = "Identifier of the language, used in the URLs of the API",
            requiredMode = REQUIRED)
        Long id,
    @Schema(
            description = "ISO 639-2 code of the language",
            example = "spa",
            requiredMode = REQUIRED)
        String iso6392,
    @Schema(
            description = "ISO 639-1 code of the language, null if it has none",
            example = "es",
            nullable = true,
            requiredMode = REQUIRED)
        String iso6391,
    @Schema(description = "Name of the language", example = "Spanish", requiredMode = REQUIRED)
        String name) {}
