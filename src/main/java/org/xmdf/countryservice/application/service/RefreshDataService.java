package org.xmdf.countryservice.application.service;

import org.springframework.stereotype.Service;
import org.xmdf.countryservice.application.model.RefreshExecution;
import org.xmdf.countryservice.application.port.in.RefreshDataUseCase;
import org.xmdf.countryservice.application.port.out.DataRefreshLauncher;

@Service
public class RefreshDataService implements RefreshDataUseCase {

  private final DataRefreshLauncher launcher;

  public RefreshDataService(DataRefreshLauncher launcher) {
    this.launcher = launcher;
  }

  @Override
  public RefreshExecution refresh() {
    return launcher.start();
  }
}
