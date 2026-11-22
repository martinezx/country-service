package org.xmdf.countryservice.domain;

/**
 * A timezone.
 *
 * @param id the identifier assigned when the timezone is stored, or null if it has not been stored
 * @param timezone the identifier of the timezone as published by the source, for example {@code
 *     UTC+01:00}. It identifies the timezone among the timezones.
 */
public record Timezone(Long id, String timezone) {}
