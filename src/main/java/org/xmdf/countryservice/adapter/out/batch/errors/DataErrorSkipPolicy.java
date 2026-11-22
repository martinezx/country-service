package org.xmdf.countryservice.adapter.out.batch.errors;

import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.application.exception.InvalidRecordException;
import org.xmdf.countryservice.domain.UnsupportedTimezoneException;

@Component
public class DataErrorSkipPolicy implements SkipPolicy {

  @Override
  public boolean shouldSkip(Throwable throwable, long skipCount) {
    return throwable instanceof InvalidRecordException
        || throwable instanceof UnsupportedTimezoneException
        || throwable instanceof DataIntegrityViolationException
        || throwable instanceof FlatFileParseException;
  }
}
