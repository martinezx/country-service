package org.xmdf.countryservice.application.port.in;

import org.xmdf.countryservice.application.exception.RefreshAlreadyRunningException;
import org.xmdf.countryservice.application.model.RefreshExecution;

/**
 * Use case that refreshes the stored country data from countries.dev.
 *
 * <p>A refresh downloads the regions, timezones, currencies, languages and countries and stores
 * them, inserting new records and updating existing ones. Only one refresh may run at a time.
 */
public interface RefreshDataUseCase {

  /**
   * Starts a refresh. The refresh runs asynchronously, so this method returns as soon as the
   * refresh has been started, without waiting for it to finish.
   *
   * @return a handle to the started refresh
   * @throws RefreshAlreadyRunningException if another refresh is still in progress
   */
  RefreshExecution refresh();
}
