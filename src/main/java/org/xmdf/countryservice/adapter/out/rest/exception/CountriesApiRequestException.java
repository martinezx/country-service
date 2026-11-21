package org.xmdf.countryservice.adapter.out.rest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CountriesApiRequestException extends CountriesApiException {

  private final HttpStatusCode status;

  public CountriesApiRequestException(String message, HttpStatusCode status, Throwable cause) {
    super(message, cause);
    this.status = status;
  }
}
