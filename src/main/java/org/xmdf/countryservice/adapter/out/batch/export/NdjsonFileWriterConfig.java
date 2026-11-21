package org.xmdf.countryservice.adapter.out.batch.export;

import com.fasterxml.jackson.databind.JsonNode;
import java.nio.file.Path;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.xmdf.countryservice.adapter.out.batch.shared.BatchSettings;
import org.xmdf.countryservice.adapter.out.batch.shared.ExportEntity;

@Configuration
public class NdjsonFileWriterConfig {

  @Bean
  @StepScope
  public FlatFileItemWriter<JsonNode> exportItemWriter(
      BatchSettings settings, @Value("#{stepExecutionContext['entity']}") String entity) {
    ExportEntity exportEntity = ExportEntity.valueOf(entity);
    return new FlatFileItemWriterBuilder<JsonNode>()
        .name("exportItemWriter-" + exportEntity.name())
        .resource(new FileSystemResource(Path.of(settings.outputDir(), exportEntity.fileName())))
        .lineAggregator(JsonNode::toString)
        .lineSeparator("\n")
        .encoding("UTF-8")
        .shouldDeleteIfExists(true)
        .saveState(false)
        .build();
  }
}
