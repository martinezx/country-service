package org.xmdf.countryservice.application.exception;

/**
 * Thrown when a data refresh is requested while another one is still in progress. Only one refresh
 * may run at a time.
 */
public class RefreshAlreadyRunningException extends RuntimeException {

  public RefreshAlreadyRunningException() {
    super("A data refresh is already running");
  }
}
