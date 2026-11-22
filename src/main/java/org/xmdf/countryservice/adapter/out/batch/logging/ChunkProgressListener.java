package org.xmdf.countryservice.adapter.out.batch.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChunkProgressListener implements ChunkListener {

  @Override
  public void afterChunk(ChunkContext context) {
    StepExecution step = context.getStepContext().getStepExecution();
    log.debug(
        "Step {} chunk committed: read={}, written={}, filtered={}, commits={}, rollbacks={}",
        step.getStepName(),
        step.getReadCount(),
        step.getWriteCount(),
        step.getFilterCount(),
        step.getCommitCount(),
        step.getRollbackCount());
  }

  @Override
  public void afterChunkError(ChunkContext context) {
    StepExecution step = context.getStepContext().getStepExecution();
    log.debug(
        "Step {} chunk rolled back: read={}, written={}, commits={}, rollbacks={}",
        step.getStepName(),
        step.getReadCount(),
        step.getWriteCount(),
        step.getCommitCount(),
        step.getRollbackCount());
  }
}
