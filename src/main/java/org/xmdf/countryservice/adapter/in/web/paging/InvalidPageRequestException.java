package org.xmdf.countryservice.adapter.in.web.paging;

/** Thrown when the paging parameters of a request are invalid. */
public class InvalidPageRequestException extends RuntimeException {

  public InvalidPageRequestException(String message) {
    super(message);
  }
}
