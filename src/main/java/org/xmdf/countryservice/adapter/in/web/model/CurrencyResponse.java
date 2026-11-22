package org.xmdf.countryservice.adapter.in.web.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

/**
 * Representation of a currency.
 *
 * @param id the identifier of the currency, used in the URLs of the API
 * @param code the currency code
 * @param name the name of the currency
 * @param symbol the symbol of the currency, or null if it has none
 */
@Relation(itemRelation = "currency", collectionRelation = "currencies")
public record CurrencyResponse(
    @Schema(
            description = "Identifier of the currency, used in the URLs of the API",
            requiredMode = REQUIRED)
        Long id,
    @Schema(
            description =
                "Currency code, usually an ISO 4217 code of three letters. The source also"
                    + " publishes longer codes such as IMP[G].",
            example = "EUR",
            requiredMode = REQUIRED)
        String code,
    @Schema(description = "Name of the currency", example = "Euro", requiredMode = REQUIRED)
        String name,
    @Schema(
            description = "Symbol of the currency, null if it has none",
            example = "€",
            nullable = true,
            requiredMode = REQUIRED)
        String symbol) {}
