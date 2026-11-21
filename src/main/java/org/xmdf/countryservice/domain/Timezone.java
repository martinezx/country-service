package org.xmdf.countryservice.domain;

/**
 * A timezone.
 *
 * @param timezone the identifier of the timezone as published by the source, for example {@code
 *     UTC+01:00}. It identifies the timezone.
 */
public record Timezone(String timezone) {}
