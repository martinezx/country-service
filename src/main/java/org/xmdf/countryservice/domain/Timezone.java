package org.xmdf.countryservice.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A timezone.
 *
 * @param id the identifier assigned when the timezone is stored, or null if it has not been stored
 * @param timezone the identifier of the timezone as published by the source, for example {@code
 *     UTC+01:00}, or {@code UTC} for no offset. It identifies the timezone among the timezones.
 */
public record Timezone(Long id, String timezone) {

  private static final Pattern IDENTIFIER = Pattern.compile("UTC(?:([+-])(\\d{2}):(\\d{2}))?");

  /**
   * Returns the offset from UTC in minutes. It is positive east of UTC and negative west of it.
   *
   * @return the offset in minutes, for example 330 for {@code UTC+05:30} and -720 for {@code
   *     UTC-12:00}
   * @throws IllegalArgumentException if the identifier does not have the form {@code UTC} or
   *     {@code UTC±HH:MM}
   */
  public int utcOffsetMinutes() {
    Matcher matcher = IDENTIFIER.matcher(timezone);
    boolean validMinutes = matcher.matches() && (matcher.group(3) == null || minutes(matcher) < 60);
    if (!validMinutes) {
      throw new IllegalArgumentException("Unsupported timezone identifier: " + timezone);
    }
    if (matcher.group(1) == null) {
      return 0;
    }
    int minutes = Integer.parseInt(matcher.group(2)) * 60 + minutes(matcher);
    return matcher.group(1).equals("-") ? -minutes : minutes;
  }

  private static int minutes(Matcher matcher) {
    return Integer.parseInt(matcher.group(3));
  }

  /**
   * Returns the offset from UTC as text.
   *
   * @return the offset in the form {@code +HH:MM} or {@code -HH:MM}, with {@code +00:00} for no
   *     offset
   * @throws IllegalArgumentException if the identifier does not have the form {@code UTC} or
   *     {@code UTC±HH:MM}
   */
  public String utcOffset() {
    int minutes = utcOffsetMinutes();
    int absolute = Math.abs(minutes);
    return "%s%02d:%02d".formatted(minutes < 0 ? "-" : "+", absolute / 60, absolute % 60);
  }
}
