package org.xmdf.countryservice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xmdf.countryservice.application.model.RefreshExecution;
import org.xmdf.countryservice.application.port.in.RefreshDataUseCase;
import org.xmdf.countryservice.application.port.out.DataRefreshLauncher;

@Service
@RequiredArgsConstructor
public class RefreshDataService implements RefreshDataUseCase {

  private final DataRefreshLauncher launcher;

  @Override
  public RefreshExecution refresh() {
    return launcher.start();
  }
}
