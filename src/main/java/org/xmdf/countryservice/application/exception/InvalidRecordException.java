package org.xmdf.countryservice.application.exception;

/**
 * Thrown when a record cannot be stored because its data is invalid, or because it refers to data
 * that does not exist. The problem is specific to that record, so other records can still be
 * stored.
 */
public class InvalidRecordException extends RuntimeException {

  public InvalidRecordException(String message) {
    super(message);
  }

  public InvalidRecordException(String message, Throwable cause) {
    super(message, cause);
  }
}
