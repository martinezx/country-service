package org.xmdf.countryservice.adapter.out.batch.shared;

import static org.xmdf.countryservice.adapter.out.batch.shared.BatchSettings.CHUNK_SIZE;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChunkPolicyConfig {

  @Bean
  @StepScope
  public CompletionPolicy chunkCompletionPolicy(
      @Value("#{stepExecutionContext['entity']}") String entity) {
    return new SimpleCompletionPolicy(
        ExportEntity.valueOf(entity).chunked() ? CHUNK_SIZE : Integer.MAX_VALUE);
  }
}
