package org.xmdf.countryservice.adapter.out.batch.load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.xmdf.countryservice.adapter.out.batch.shared.BatchSettings;
import org.xmdf.countryservice.adapter.out.batch.shared.ExportEntity;

@Configuration
public class NdjsonFileReaderConfig {

  @Bean
  @StepScope
  public FlatFileItemReader<JsonNode> loadReferenceItemReader(
      ObjectMapper objectMapper,
      BatchSettings settings,
      @Value("#{stepExecutionContext['entity']}") String entity) {
    return ndjsonReader(objectMapper, settings, ExportEntity.valueOf(entity));
  }

  @Bean
  @StepScope
  public FlatFileItemReader<JsonNode> loadCountryItemReader(
      ObjectMapper objectMapper, BatchSettings settings) {
    return ndjsonReader(objectMapper, settings, ExportEntity.COUNTRY);
  }

  private static FlatFileItemReader<JsonNode> ndjsonReader(
      ObjectMapper objectMapper, BatchSettings settings, ExportEntity entity) {
    return new FlatFileItemReaderBuilder<JsonNode>()
        .name("loadItemReader-" + entity.name())
        .resource(new FileSystemResource(Path.of(settings.outputDir(), entity.fileName())))
        .lineMapper((line, lineNumber) -> objectMapper.readTree(line))
        .encoding("UTF-8")
        .build();
  }
}
