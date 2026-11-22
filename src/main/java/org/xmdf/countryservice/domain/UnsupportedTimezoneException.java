package org.xmdf.countryservice.domain;

/** Thrown when the identifier of a timezone does not have a form whose offset is known. */
public class UnsupportedTimezoneException extends RuntimeException {

  public UnsupportedTimezoneException(String timezone) {
    super("Unsupported timezone identifier: " + timezone);
  }
}
