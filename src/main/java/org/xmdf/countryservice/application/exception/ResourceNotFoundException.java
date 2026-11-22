package org.xmdf.countryservice.application.exception;

/** Thrown when a requested resource does not exist. */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String resource, Long id) {
    super("%s %d does not exist".formatted(resource, id));
  }
}
