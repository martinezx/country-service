package org.xmdf.countryservice.adapter.out.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.autoconfigure.batch.BatchTaskExecutor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.xmdf.countryservice.adapter.out.batch.errors.RefreshVerdictTasklet;
import org.xmdf.countryservice.adapter.out.batch.shared.BatchSettings;
import org.xmdf.countryservice.adapter.out.batch.shared.StepNames;

@Configuration
@EnableConfigurationProperties(BatchSettings.class)
public class DataRefreshJobConfig {

  public static final String JOB_NAME = "dataRefresh";

  @Bean
  public Job dataRefreshJob(
      JobRepository jobRepository,
      Step exportManagerStep,
      Step loadReferenceManagerStep,
      Step loadCountryStep,
      Step verdictStep) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(exportManagerStep)
        .on("*")
        .to(loadReferenceManagerStep)
        .from(loadReferenceManagerStep)
        .on("*")
        .to(loadCountryStep)
        .from(loadCountryStep)
        .on("*")
        .to(verdictStep)
        .from(verdictStep)
        .on("FAILED")
        .fail()
        .from(verdictStep)
        .on("*")
        .end()
        .end()
        .build();
  }

  @Bean
  public Step verdictStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      RefreshVerdictTasklet verdict) {
    return new StepBuilder(StepNames.VERDICT, jobRepository)
        .tasklet(verdict, transactionManager)
        .build();
  }

  @Bean
  @BatchTaskExecutor
  public TaskExecutor batchLauncherTaskExecutor() {
    return new SimpleAsyncTaskExecutor("job-launcher-");
  }
}
