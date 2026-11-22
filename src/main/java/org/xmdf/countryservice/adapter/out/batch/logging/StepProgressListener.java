package org.xmdf.countryservice.adapter.out.batch.logging;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StepProgressListener implements StepExecutionListener {

  @Override
  public void beforeStep(StepExecution step) {
    log.info("Step {} started", step.getStepName());
  }

  @Override
  public ExitStatus afterStep(StepExecution step) {
    long processed = step.getReadCount() + step.getReadSkipCount();
    long failed = processed - step.getWriteCount() - step.getFilterCount();
    log.info(
        "Step {} {} in {} ms: processed={}, successful={}, failed={}, filtered={}, commits={},"
            + " rollbacks={}",
        step.getStepName(),
        step.getStatus(),
        Duration.between(step.getStartTime(), step.getEndTime()).toMillis(),
        processed,
        step.getWriteCount(),
        failed,
        step.getFilterCount(),
        step.getCommitCount(),
        step.getRollbackCount());
    return null;
  }
}
