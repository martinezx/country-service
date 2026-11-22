package org.xmdf.countryservice.adapter.in.web.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

/**
 * Representation of a region.
 *
 * @param id the identifier of the region, used in the URLs of the API
 * @param name the name of the region
 */
@Relation(itemRelation = "region", collectionRelation = "regions")
public record RegionResponse(
    @Schema(
            description = "Identifier of the region, used in the URLs of the API",
            requiredMode = REQUIRED)
        Long id,
    @Schema(description = "Name of the region", example = "Europe", requiredMode = REQUIRED)
        String name) {}
