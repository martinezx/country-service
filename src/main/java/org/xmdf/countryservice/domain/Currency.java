package org.xmdf.countryservice.domain;

/**
 * A currency.
 *
 * @param code the currency code, which identifies the currency. It is usually an ISO 4217 code of
 *     three letters, but the source also publishes longer codes such as {@code IMP[G]}.
 * @param name the name of the currency
 * @param symbol the symbol of the currency, or null if it has none
 */
public record Currency(String code, String name, String symbol) {}
