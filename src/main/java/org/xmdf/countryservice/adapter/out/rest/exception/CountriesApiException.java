package org.xmdf.countryservice.adapter.out.rest.exception;

public abstract class CountriesApiException extends RuntimeException {

  protected CountriesApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
