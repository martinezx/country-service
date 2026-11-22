package org.xmdf.countryservice.adapter.out.batch.errors;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.application.exception.InvalidRecordException;
import org.xmdf.countryservice.domain.UnsupportedTimezoneException;

@Slf4j
@Component
public class SkippedRecordListener implements SkipListener<JsonNode, JsonNode> {

  @Override
  public void onSkipInRead(Throwable throwable) {
    if (throwable instanceof FlatFileParseException parseException) {
      log.warn(
          "Step {} skipped line {}: {} | record: {}",
          stepName(),
          parseException.getLineNumber(),
          reason(throwable),
          parseException.getInput());
    } else {
      log.warn(
          "Step {} skipped a record that could not be read: {}", stepName(), reason(throwable));
    }
  }

  @Override
  public void onSkipInWrite(JsonNode item, Throwable throwable) {
    log.warn("Step {} skipped a record: {} | record: {}", stepName(), reason(throwable), item);
  }

  private static String stepName() {
    StepContext context = StepSynchronizationManager.getContext();
    return context == null ? "?" : context.getStepName();
  }

  private static String reason(Throwable throwable) {
    Throwable source =
        throwable instanceof InvalidRecordException
                || throwable instanceof UnsupportedTimezoneException
            ? throwable
            : NestedExceptionUtils.getMostSpecificCause(throwable);
    String message = String.valueOf(source.getMessage());
    return message.replaceAll("\\s+", " ");
  }
}
