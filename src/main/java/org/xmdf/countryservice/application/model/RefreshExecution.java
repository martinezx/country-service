package org.xmdf.countryservice.application.model;

/**
 * Handle to a data refresh that has been started.
 *
 * @param id the identifier of the refresh
 * @param status the status of the refresh at the moment it was started, for example {@code
 *     STARTING}. It does not change afterwards.
 */
public record RefreshExecution(Long id, String status) {}
