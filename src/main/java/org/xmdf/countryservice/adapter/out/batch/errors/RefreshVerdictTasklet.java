package org.xmdf.countryservice.adapter.out.batch.errors;

import static org.xmdf.countryservice.adapter.out.batch.shared.StepNames.EXPORT_WORKER;
import static org.xmdf.countryservice.adapter.out.batch.shared.StepNames.LOAD_COUNTRY;
import static org.xmdf.countryservice.adapter.out.batch.shared.StepNames.LOAD_REFERENCE_WORKER;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.adapter.out.batch.shared.BatchSettings;

@Slf4j
@Component
public class RefreshVerdictTasklet implements Tasklet {

  private final BatchSettings settings;

  public RefreshVerdictTasklet(BatchSettings settings) {
    this.settings = settings;
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    List<StepExecution> steps =
        chunkContext
            .getStepContext()
            .getStepExecution()
            .getJobExecution()
            .getStepExecutions()
            .stream()
            .filter(RefreshVerdictTasklet::isDataStep)
            .toList();

    List<String> problems = new ArrayList<>();
    long skipped = 0;
    for (StepExecution step : steps) {
      skipped += step.getSkipCount();
      String problem = problem(step);
      if (problem != null) {
        problems.add(problem);
      }
    }

    if (problems.isEmpty()) {
      log.info(
          "Data refresh checked {} steps: {} records skipped, all within budget",
          steps.size(),
          skipped);
    } else {
      problems.forEach(problem -> log.warn("Data refresh problem: {}", problem));
      contribution.setExitStatus(new ExitStatus("FAILED", String.join("; ", problems)));
    }
    return RepeatStatus.FINISHED;
  }

  private String problem(StepExecution step) {
    long processed = step.getReadCount() + step.getReadSkipCount();
    if (step.getStatus() != BatchStatus.COMPLETED) {
      return "%s %s".formatted(step.getStepName(), step.getExitStatus().getExitCode());
    }
    if (processed == 0) {
      return "%s processed no records".formatted(step.getStepName());
    }
    double rate = (double) step.getSkipCount() / processed;
    if (step.getSkipCount() > 0 && rate > settings.maxErrorRate()) {
      return "%s skipped %d of %d records (%.1f%%, budget %.1f%%)"
          .formatted(
              step.getStepName(),
              step.getSkipCount(),
              processed,
              rate * 100,
              settings.maxErrorRate() * 100);
    }
    return null;
  }

  private static boolean isDataStep(StepExecution step) {
    String name = step.getStepName();
    return name.startsWith(EXPORT_WORKER + ":")
        || name.startsWith(LOAD_REFERENCE_WORKER + ":")
        || name.equals(LOAD_COUNTRY);
  }
}
