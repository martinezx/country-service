package org.xmdf.countryservice.rest.exception;

public abstract class CountriesApiException extends RuntimeException {

  protected CountriesApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
