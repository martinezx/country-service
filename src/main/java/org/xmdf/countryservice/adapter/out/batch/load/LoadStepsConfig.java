package org.xmdf.countryservice.adapter.out.batch.load;

import static org.xmdf.countryservice.adapter.out.batch.shared.BatchSettings.CHUNK_SIZE;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.xmdf.countryservice.adapter.out.batch.logging.ChunkProgressListener;
import org.xmdf.countryservice.adapter.out.batch.logging.StepProgressListener;
import org.xmdf.countryservice.adapter.out.batch.shared.EntityPartitioner;
import org.xmdf.countryservice.adapter.out.batch.shared.ExportEntity;

@Configuration
public class LoadStepsConfig {

  @Bean
  public Step loadReferenceManagerStep(
      JobRepository jobRepository,
      Step loadReferenceWorkerStep,
      StepProgressListener stepProgress) {
    return new StepBuilder("loadReferenceManagerStep", jobRepository)
        .partitioner(
            "loadReferenceWorkerStep",
            new EntityPartitioner(
                ExportEntity.REGION,
                ExportEntity.TIMEZONE,
                ExportEntity.CURRENCY,
                ExportEntity.LANGUAGE))
        .step(loadReferenceWorkerStep)
        .gridSize(4)
        .taskExecutor(new SimpleAsyncTaskExecutor("load-"))
        .listener(stepProgress)
        .build();
  }

  @Bean
  public Step loadReferenceWorkerStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      CompletionPolicy chunkCompletionPolicy,
      FlatFileItemReader<JsonNode> loadReferenceItemReader,
      StoreItemWriter<?, ?> loadReferenceItemWriter,
      StepProgressListener stepProgress,
      ChunkProgressListener chunkProgress) {
    return new StepBuilder("loadReferenceWorkerStep", jobRepository)
        .<JsonNode, JsonNode>chunk(chunkCompletionPolicy, transactionManager)
        .reader(loadReferenceItemReader)
        .writer(loadReferenceItemWriter)
        .listener(stepProgress)
        .listener(chunkProgress)
        .build();
  }

  @Bean
  public Step loadCountryStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      FlatFileItemReader<JsonNode> loadCountryItemReader,
      StoreItemWriter<?, ?> loadCountryItemWriter,
      StepProgressListener stepProgress,
      ChunkProgressListener chunkProgress) {
    return new StepBuilder("loadCountryStep", jobRepository)
        .<JsonNode, JsonNode>chunk(CHUNK_SIZE, transactionManager)
        .reader(loadCountryItemReader)
        .writer(loadCountryItemWriter)
        .listener(stepProgress)
        .listener(chunkProgress)
        .build();
  }

  @Bean
  @StepScope
  StoreItemWriter<?, ?> loadReferenceItemWriter(
      StoreItemWriters writers, @Value("#{stepExecutionContext['entity']}") String entity) {
    return writers.forEntity(ExportEntity.valueOf(entity));
  }

  @Bean
  StoreItemWriter<?, ?> loadCountryItemWriter(StoreItemWriters writers) {
    return writers.forEntity(ExportEntity.COUNTRY);
  }
}
