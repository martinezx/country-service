package org.xmdf.countryservice.application.port.out;

import org.xmdf.countryservice.application.exception.RefreshAlreadyRunningException;
import org.xmdf.countryservice.application.model.RefreshExecution;

/**
 * Starts the process that refreshes the stored country data. Implementations decide how the refresh
 * is executed, and must run it asynchronously.
 */
public interface DataRefreshLauncher {

  /**
   * Starts a refresh and returns without waiting for it to finish. At most one refresh may run at a
   * time.
   *
   * @return a handle to the started refresh
   * @throws RefreshAlreadyRunningException if another refresh is still in progress
   */
  RefreshExecution start();
}
