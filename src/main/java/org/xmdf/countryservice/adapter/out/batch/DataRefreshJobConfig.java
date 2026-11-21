package org.xmdf.countryservice.adapter.out.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.batch.BatchTaskExecutor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.xmdf.countryservice.adapter.out.batch.shared.BatchSettings;

@Configuration
@EnableConfigurationProperties(BatchSettings.class)
public class DataRefreshJobConfig {

  public static final String JOB_NAME = "dataRefresh";

  @Bean
  public Job dataRefreshJob(
      JobRepository jobRepository,
      Step exportManagerStep,
      Step loadReferenceManagerStep,
      Step loadCountryStep) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(exportManagerStep)
        .next(loadReferenceManagerStep)
        .next(loadCountryStep)
        .build();
  }

  @Bean
  @BatchTaskExecutor
  public TaskExecutor batchLauncherTaskExecutor() {
    return new SimpleAsyncTaskExecutor("job-launcher-");
  }
}
