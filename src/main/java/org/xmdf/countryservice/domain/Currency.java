package org.xmdf.countryservice.domain;

/**
 * A currency.
 *
 * @param id the identifier assigned when the currency is stored, or null if it has not been stored
 * @param code the currency code, which identifies the currency among the currencies. It is usually
 *     an ISO 4217 code of three letters, but the source also publishes longer codes such as {@code
 *     IMP[G]}.
 * @param name the name of the currency
 * @param symbol the symbol of the currency, or null if it has none
 */
public record Currency(Long id, String code, String name, String symbol) {}
