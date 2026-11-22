package org.xmdf.countryservice.adapter.out.batch.export;

import static org.xmdf.countryservice.adapter.out.batch.shared.BatchSettings.CHUNK_SIZE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.function.BiFunction;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.xmdf.countryservice.adapter.out.batch.logging.ChunkProgressListener;
import org.xmdf.countryservice.adapter.out.batch.logging.StepProgressListener;
import org.xmdf.countryservice.adapter.out.batch.ndjson.NdjsonMapper;
import org.xmdf.countryservice.adapter.out.batch.shared.EntityPartitioner;
import org.xmdf.countryservice.adapter.out.batch.shared.ExportEntity;
import org.xmdf.countryservice.application.port.out.CountriesSource;

@Configuration
public class ExportStepsConfig {

  @Bean
  public Step exportManagerStep(
      JobRepository jobRepository, Step exportWorkerStep, StepProgressListener stepProgress) {
    return new StepBuilder("exportManagerStep", jobRepository)
        .partitioner("exportWorkerStep", new EntityPartitioner(ExportEntity.values()))
        .step(exportWorkerStep)
        .gridSize(ExportEntity.values().length)
        .taskExecutor(new SimpleAsyncTaskExecutor("export-"))
        .listener(stepProgress)
        .build();
  }

  @Bean
  public Step exportWorkerStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      CompletionPolicy chunkCompletionPolicy,
      CountriesSourceItemReader exportItemReader,
      FlatFileItemWriter<JsonNode> exportItemWriter,
      StepProgressListener stepProgress,
      ChunkProgressListener chunkProgress) {
    return new StepBuilder("exportWorkerStep", jobRepository)
        .<JsonNode, JsonNode>chunk(chunkCompletionPolicy, transactionManager)
        .reader(exportItemReader)
        .writer(exportItemWriter)
        .listener(stepProgress)
        .listener(chunkProgress)
        .build();
  }

  @Bean
  @StepScope
  public CountriesSourceItemReader exportItemReader(
      CountriesSource source,
      NdjsonMapper mapper,
      ObjectMapper objectMapper,
      @Value("#{stepExecutionContext['entity']}") String entity) {
    ExportEntity exportEntity = ExportEntity.valueOf(entity);
    BiFunction<Integer, Integer, List<?>> fetcher =
        switch (exportEntity) {
          case REGION ->
              (limit, offset) -> source.fetchRegions().stream().map(mapper::toLine).toList();
          case TIMEZONE ->
              (limit, offset) -> source.fetchTimezones().stream().map(mapper::toLine).toList();
          case CURRENCY ->
              (limit, offset) ->
                  source.fetchCurrencies(limit, offset).stream().map(mapper::toLine).toList();
          case LANGUAGE ->
              (limit, offset) ->
                  source.fetchLanguages(limit, offset).stream().map(mapper::toLine).toList();
          case COUNTRY ->
              (limit, offset) ->
                  source.fetchCountries(limit, offset).stream().map(mapper::toLine).toList();
        };
    return new CountriesSourceItemReader(fetcher, objectMapper, exportEntity.chunked(), CHUNK_SIZE);
  }
}
