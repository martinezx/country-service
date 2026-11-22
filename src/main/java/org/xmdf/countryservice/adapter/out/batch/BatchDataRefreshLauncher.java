package org.xmdf.countryservice.adapter.out.batch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.application.exception.RefreshAlreadyRunningException;
import org.xmdf.countryservice.application.model.RefreshExecution;
import org.xmdf.countryservice.application.port.out.DataRefreshLauncher;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class BatchDataRefreshLauncher implements DataRefreshLauncher {

  private final JobLauncher jobLauncher;
  private final JobExplorer jobExplorer;
  private final Job dataRefreshJob;

  @Override
  public synchronized RefreshExecution start() {
    if (!jobExplorer.findRunningJobExecutions(DataRefreshJobConfig.JOB_NAME).isEmpty()) {
      throw new RefreshAlreadyRunningException();
    }
    JobParameters parameters =
        new JobParametersBuilder()
            .addLong("startedAt", System.currentTimeMillis())
            .toJobParameters();
    try {
      JobExecution execution = jobLauncher.run(dataRefreshJob, parameters);
      return new RefreshExecution(execution.getId(), execution.getStatus().name());
    } catch (Exception e) {
      throw new IllegalStateException("Could not launch " + DataRefreshJobConfig.JOB_NAME, e);
    }
  }
}
